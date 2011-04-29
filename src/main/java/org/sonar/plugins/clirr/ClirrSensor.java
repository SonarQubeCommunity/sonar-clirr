/*
 * Sonar Clirr Plugin
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

/*
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.clirr;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.SonarException;
import org.sonar.java.api.JavaClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ClirrSensor implements Sensor, DependsUponMavenPlugin {

  private final ClirrConfiguration configuration;
  private final ClirrMavenPluginHandler clirrMavenHandler;

  public ClirrSensor(ClirrConfiguration configuration, ClirrMavenPluginHandler mavenHandler) {
    this.configuration = configuration;
    this.clirrMavenHandler = mavenHandler;
  }

  public boolean shouldExecuteOnProject(Project project) {
    return Java.INSTANCE.equals(project.getLanguage()) && configuration.isActive();
  }

  public MavenPluginHandler getMavenPluginHandler(Project project) {
    return clirrMavenHandler;
  }

  public void analyse(Project project, SensorContext context) {
    InputStream input = null;
    try {
      File report = new File(project.getFileSystem().getSonarWorkingDirectory(), ClirrConstants.RESULT_TXT);
      if (report.exists()) {
        input = new FileInputStream(report);

        ClirrTxtResultParser parser = new ClirrTxtResultParser();
        List<ClirrViolation> violations = parser.parse(input, project.getFileSystem().getSourceCharset());
        saveViolations(violations, context, project);

      } else {
        LoggerFactory.getLogger(getClass()).info("Clirr report does not exist: " + report.getCanonicalPath());
      }

    } catch (IOException e) {
      throw new SonarException("Clirr report can not be loaded.", e);

    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  protected void saveViolations(final List<ClirrViolation> violations, final SensorContext context, final Project project) {
    for (ClirrViolation violation : violations) {
      String ruleKey = violation.getRuleKey();
      ActiveRule activeRule = configuration.getActiveRule(ruleKey);
      if (activeRule != null) {
        JavaClass javaClass = violation.getJavaClass();
        JavaClass indexedJavaClass = context.getResource(javaClass);
        Resource resource = project;
        if (indexedJavaClass!=null) {
          resource = context.getParent(indexedJavaClass);// parent is a Java file
        }
        context.saveViolation(Violation.create(activeRule, resource).setMessage(violation.getMessage()));
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
