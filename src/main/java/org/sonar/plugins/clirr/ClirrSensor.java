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
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.SonarException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ClirrSensor implements Sensor, DependsUponMavenPlugin {

  private final ClirrConfiguration configuration;
  private final ClirrMavenPluginHandler clirrMavenHandler;
  private final ClirrRulesRepository rulesRepo;

  public ClirrSensor(ClirrConfiguration configuration, ClirrMavenPluginHandler mavenHandler,
                     ClirrRulesRepository rulesRepo) {
    this.configuration = configuration;
    this.clirrMavenHandler = mavenHandler;
    this.rulesRepo = rulesRepo;
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
      Rule rule = rulesRepo.getRuleFromClirrViolation(violation);
      ActiveRule activeRule = configuration.getActiveRule(rule.getKey());
      if (activeRule != null) {
        Resource<?> resource = violation.getJavaFile();
        if (context.getResource(resource) == null) {
          resource = project;
        }
        context.saveViolation(new Violation(activeRule.getRule(), resource).setMessage(violation.getMessage()));
      }
    }
  }

}