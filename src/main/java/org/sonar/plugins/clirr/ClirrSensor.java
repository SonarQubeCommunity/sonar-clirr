package org.sonar.plugins.clirr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.MavenPluginExecutor;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.JavaClass;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.SonarException;

public final class ClirrSensor implements Sensor {

	private final RulesProfile rulesProfile;
	private final MavenPluginExecutor executor;
	private final ClirrMavenPluginHandler clirrMavenHandler;
	private final ClirrRulesRepository rulesRepo;

	public ClirrSensor(RulesProfile rulesProfile, MavenPluginExecutor executor, ClirrMavenPluginHandler mavenHandler,
			ClirrRulesRepository rulesRepo) {
		this.rulesProfile = rulesProfile;
		this.executor = executor;
		this.clirrMavenHandler = mavenHandler;
		this.rulesRepo = rulesRepo;
	}

	public void analyse(Project project, SensorContext context) {
		try {
			executor.execute(clirrMavenHandler);
			ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
			File result = new File(project.getBuildDir(), ClirrPlugin.CLIRR_RESULT_TXT);
			List<ClirrViolation> violations = clirrParser
					.parse(new FileInputStream(result), project.getSourceCharset());
			for (ClirrViolation violation : violations) {
				Rule rule = rulesRepo.getRuleFromClirrViolation(violation);
				ActiveRule activeRule = rulesProfile.getActiveRule(ClirrPlugin.CLIRR_PLUGIN_KEY, rule.getKey());
				if (activeRule != null) {
					context.saveViolation(new JavaClass(violation.getAffectedClass()), rule, violation
							.getMessage(), activeRule.getPriority(), 0);
				}
			}
		} catch (IOException e) {
			throw new SonarException("Clirr result file could not be read", e);
		}
	}

	public boolean shouldExecuteOnProject(Project project) {
		return project.getLanguage().equals(Java.KEY)
				&& project.getConfiguration().getBoolean(ClirrPlugin.CLIRR_KEY_EXECUTE, false);
	}

}