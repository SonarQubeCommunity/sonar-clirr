package org.sonar.plugins.clirr;

import org.apache.commons.io.IOUtils;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.profiles.RulesProfile;
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

  private final RulesProfile rulesProfile;
  private final ClirrMavenPluginHandler clirrMavenHandler;
  private final ClirrRulesRepository rulesRepo;

  public ClirrSensor(RulesProfile rulesProfile, ClirrMavenPluginHandler mavenHandler,
                     ClirrRulesRepository rulesRepo) {
    this.rulesProfile = rulesProfile;
    this.clirrMavenHandler = mavenHandler;
    this.rulesRepo = rulesRepo;
  }

  public boolean shouldExecuteOnProject(Project project) {
    return project.getLanguage().equals(Java.INSTANCE)
        && project.getConfiguration().getBoolean(ClirrPlugin.CLIRR_KEY_EXECUTE, false);
  }

  public MavenPluginHandler getMavenPluginHandler(Project project) {
    return clirrMavenHandler;
  }

  @DependsUpon
  public String dependsUponSquidForResources() {
    return Sensor.FLAG_SQUID_ANALYSIS;
  }


  public void analyse(Project project, SensorContext context) {
    InputStream input = null;
    try {
      File result = new File(project.getFileSystem().getSonarWorkingDirectory(), ClirrPlugin.CLIRR_RESULT_TXT);
      input = new FileInputStream(result);

      ClirrTxtResultParser parser = new ClirrTxtResultParser();
      List<ClirrViolation> violations = parser.parse(input, project.getFileSystem().getSourceCharset());
      saveViolations(violations, context, project);

    } catch (IOException e) {
      throw new SonarException("Clirr result file could not be read", e);

    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  protected void saveViolations(final List<ClirrViolation> violations, final SensorContext context, final Project project) {
    for (ClirrViolation violation : violations) {
      Rule rule = rulesRepo.getRuleFromClirrViolation(violation);
      ActiveRule activeRule = rulesProfile.getActiveRule(ClirrPlugin.CLIRR_PLUGIN_KEY, rule.getKey());
      if (activeRule != null) {
        Resource resource = violation.getJavaFile();
        if (context.getResource(resource.getKey()) == null) {
          resource = project;
        }
        context.saveViolation(new Violation(activeRule.getRule(), resource).setMessage(violation.getMessage()));
      }
    }
  }

}