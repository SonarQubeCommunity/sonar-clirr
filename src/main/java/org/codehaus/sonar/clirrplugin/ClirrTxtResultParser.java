package org.codehaus.sonar.clirrplugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.sonar.commons.resources.Resource;
import org.sonar.commons.rules.Rule;
import org.sonar.commons.rules.RuleFailureLevel;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.rules.RulesManager;

public class ClirrTxtResultParser {

	private final RulesManager rulesManager;

	private final ProjectContext projectContext;

	public ClirrTxtResultParser(ProjectContext projectContext, RulesManager rulesManager) {
		this.projectContext = projectContext;
		this.rulesManager = rulesManager;
	}

	public int parse(InputStream inputStream) {
		Charset charset = Charset.defaultCharset();
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream, charset));
		int incompatibilities = 0;
		try {
			String line = reader.readLine();
			while (line != null) {
				String[] split = line.split(Pattern.quote(":"));
				if (split.length > 0) {
					String severity = split[0].trim();
					if (split.length > 1) {
						String id = split[1].trim();
						if (split.length > 2) {
							String className = split[2].trim();
							if (split.length > 3) {
								String message = split[3].trim();
								Resource resource = Java.newClass(className);
								Rule testRule = rulesManager.getPluginRule(ClirrPlugin.CLIRR_PLUGIN_KEY, "clirr" + id);
								if (testRule != null) {
									RuleFailureLevel level = mapClirrSeverity(severity);
									Integer lineId = Integer.valueOf(1);
									projectContext.addViolation(resource, testRule, message, level, lineId);
									incompatibilities++;
								}
							}
						}
					}
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(getClass()).error("Clirr result file could not be read", e);
		}
		return incompatibilities;
	}

	private RuleFailureLevel mapClirrSeverity(String severity) {
		if ("ERROR".equalsIgnoreCase(severity)) {
			return RuleFailureLevel.ERROR;
		}
		if ("WARNING".equalsIgnoreCase(severity)) {
			return RuleFailureLevel.WARNING;
		}
		if ("INFO".equalsIgnoreCase(severity)) {
			return RuleFailureLevel.INFO;
		}
		return RuleFailureLevel.INFO;
	}

}
