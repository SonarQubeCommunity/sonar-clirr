/*
 * SonarQube Clirr Plugin
 * Copyright (C) 2009 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.clirr;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.MessageException;
import org.sonar.plugins.java.api.JavaResourceLocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ClirrSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(ClirrSensor.class);

  private final ClirrConfiguration configuration;
  private final FileSystem fs;
  private final JavaResourceLocator javaResourceLocator;
  private final ResourcePerspectives perspectives;
  private final PathResolver pathResolver;

  public ClirrSensor(ClirrConfiguration configuration, FileSystem fileSystem, JavaResourceLocator javaResourceLocator,
    ResourcePerspectives perspectives, PathResolver pathResolver) {
    this.configuration = configuration;
    this.fs = fileSystem;
    this.javaResourceLocator = javaResourceLocator;
    this.perspectives = perspectives;
    this.pathResolver = pathResolver;
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    FilePredicates p = fs.predicates();
    return fs.hasFiles(p.and(p.hasType(Type.MAIN), p.hasLanguage("java"))) && configuration.isActive();
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    InputStream input = null;
    try {
      File report = pathResolver.relativeFile(fs.baseDir(), configuration.getReportPath());
      if (report.exists()) {
        input = new FileInputStream(report);

        ClirrTxtResultParser parser = new ClirrTxtResultParser();
        List<ClirrViolation> violations = parser.parse(input, fs.encoding());
        saveIssues(violations, context, project);

      } else {
        throw MessageException.of("Clirr report does not exist: " + report.getCanonicalPath());
      }

    } catch (IOException e) {
      throw new IllegalStateException("Clirr report can not be loaded.", e);

    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  protected void saveIssues(final List<ClirrViolation> violations, final SensorContext context, final Project project) {
    for (ClirrViolation violation : violations) {
      RuleKey ruleKey = violation.getRuleKey();
      if (configuration.isActive(ruleKey)) {
        javaResourceLocator.findResourceByClassName(violation.getAffectedClass());
        Resource resource = javaResourceLocator.findResourceByClassName(violation.getAffectedClass());
        if (resource == null) {
          // Resource is not indexed (maybe a deleted API) so report on project
          resource = project;
        }
        Issuable issuable = perspectives.as(Issuable.class, resource);
        if (issuable != null) {
          issuable.addIssue(issuable.newIssueBuilder()
            .ruleKey(violation.getRuleKey())
            .message(violation.getMessage())
            .build()
            );
        }
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
