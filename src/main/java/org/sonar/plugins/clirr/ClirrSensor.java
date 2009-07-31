package org.sonar.plugins.clirr;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.SonarException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    return project.getLanguage().equals(Java.KEY)
        && project.getConfiguration().getBoolean(ClirrPlugin.CLIRR_KEY_EXECUTE, false);
  }

  public MavenPluginHandler getMavenPluginHandler(Project project) {
    return clirrMavenHandler;
  }

  public void analyse(Project project, SensorContext context) {
    try {
      ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
      File result = new File(project.getBuildDir(), ClirrPlugin.CLIRR_RESULT_TXT);
      Map<JavaFile, List<ClirrViolation>> violationsByFile = clirrParser.parseToGetViolationsByResource(
          new FileInputStream(result), project.getSourceCharset());
      for (JavaFile JavaFile : violationsByFile.keySet()) {
        saveViolationsAndMeasures(context, JavaFile, violationsByFile.get(JavaFile));
      }

    } catch (IOException e) {
      throw new SonarException("Clirr result file could not be read", e);
    }
  }

  protected void saveViolationsAndMeasures(SensorContext context, JavaFile JavaFile, List<ClirrViolation> violations) {
    int totalApiChanges = 0;
    int apiBreaks = 0;
    int apiBehaviorChanges = 0;
    int newApi = 0;
    for (ClirrViolation violation : violations) {
      Rule rule = rulesRepo.getRuleFromClirrViolation(violation);
      ActiveRule activeRule = rulesProfile.getActiveRule(ClirrPlugin.CLIRR_PLUGIN_KEY, rule.getKey());
      if (activeRule != null) {
        totalApiChanges++;
        switch (violation.getType()) {
          case BREAK:
            apiBreaks++;
            break;
          case BEHAVIOR_CHANGE:
            apiBehaviorChanges++;
            break;
          case NEW_API:
            newApi++;
            break;
        }
        context.saveViolation(new Violation(violation.getJavaFile(), activeRule.getRule()).setMessage(violation.getMessage()));
      }
    }
    context.saveMeasure(JavaFile, ClirrMetrics.TOTAL_API_CHANGES, (double) totalApiChanges);
    context.saveMeasure(JavaFile, ClirrMetrics.API_BREAKS, (double) apiBreaks);
    context.saveMeasure(JavaFile, ClirrMetrics.API_BEHAVIOR_CHANGES, (double) apiBehaviorChanges);
    context.saveMeasure(JavaFile, ClirrMetrics.NEW_API, (double) newApi);
  }

}