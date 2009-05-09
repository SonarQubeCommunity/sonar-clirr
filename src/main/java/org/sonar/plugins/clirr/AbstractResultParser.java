package org.sonar.plugins.clirr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.LoggerFactory;
import org.sonar.commons.resources.Resource;
import org.sonar.commons.rules.Rule;
import org.sonar.commons.rules.RuleFailureLevel;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.model.MavenPom;
import org.sonar.plugins.api.rules.RulesManager;

public abstract class AbstractResultParser implements ClirrParser {

	private final ProjectContext projectContext;
	private final RulesManager rulesManager;
	private final MavenPom pom;
	private int incompatibilities = 0;

	public AbstractResultParser(MavenPom pom, ProjectContext projectContext, RulesManager rulesManager) {
		this.pom = pom;
		this.projectContext = projectContext;
		this.rulesManager = rulesManager;
	}

	protected abstract String getFilenameToParse();

	protected abstract void parse(InputStream inputStream);

	protected abstract File getFileToParse();

	public final void parse() {
		File fileToParse = getFileToParse();
		if (fileDoesNotExist(fileToParse)) {
			// No Clirr Result = good thing = no violations
			return;
		}
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileToParse);
			parse(inputStream);
		} catch (FileNotFoundException e) {
			LoggerFactory.getLogger(getClass()).error("Unable to parse Clirr result", e);
		} finally {
			ClirrPlugin.safeClose(inputStream);
		}
		return;

	}

	private boolean fileDoesNotExist(File fileToParse) {
		return fileToParse == null || !fileToParse.exists() || !fileToParse.canRead();
	}

	protected final boolean createViolation(String severity, String id, String message, Resource resource) {
		Rule testRule = rulesManager.getPluginRule(ClirrPlugin.CLIRR_PLUGIN_KEY, "clirr" + id);
		if (isRuleActive(testRule)) {
			RuleFailureLevel level = mapClirrSeverity(severity);
			// Currently no way to get correct lineId from Clirr
			Integer lineId = Integer.valueOf(1);
			projectContext.addViolation(resource, testRule, message, level, lineId);
			incompatibilities++;
			return true;
		}
		return false;
	}

	private boolean isRuleActive(Rule testRule) {
		return testRule != null;
	}

	private RuleFailureLevel mapClirrSeverity(String severity) {
		if ("ERROR".equalsIgnoreCase(severity)) {
			return RuleFailureLevel.ERROR;
		} else if ("WARNING".equalsIgnoreCase(severity)) {
			return RuleFailureLevel.WARNING;
		} else if ("INFO".equalsIgnoreCase(severity)) {
			return RuleFailureLevel.INFO;
		}
		return RuleFailureLevel.INFO;
	}

	public final ProjectContext getProjectContext() {
		return projectContext;
	}

	public final RulesManager getRulesManager() {
		return rulesManager;
	}

	public final MavenPom getMavenPom() {
		return pom;
	}

	public final int getIncompatibilities() {
		return incompatibilities;
	}
}