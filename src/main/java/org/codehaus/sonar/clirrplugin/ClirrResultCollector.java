package org.codehaus.sonar.clirrplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.LoggerFactory;
import org.sonar.plugins.api.maven.MavenCollector;
import org.sonar.plugins.api.maven.MavenPluginHandler;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.model.MavenPom;
import org.sonar.plugins.api.rules.RulesManager;

//If you begin to understand the Sonar API, the AbstractJavaMavenCollector implements org.sonar.plugins.api.Extension
public final class ClirrResultCollector implements MavenCollector {

	private final RulesManager rulesManager;

	public ClirrResultCollector(RulesManager rulesManager) {
		this.rulesManager = rulesManager;
	}

	// This is where you collect the results
	public void collect(MavenPom pom, ProjectContext context) {
		int incompatibilities = parseTextFile(pom, context, rulesManager);

		if (false) {
			// XML disabled. it does not contain ID
			incompatibilities = parseXMLFile(pom, context);
		}

		// Summary: the total number of clirr violations
		Double apiBreaks = Double.valueOf(incompatibilities);
		context.addMeasure(ClirrMetrics.APIBREAKS, apiBreaks);
	}

	private int parseXMLFile(MavenPom pom, ProjectContext context) {
		File xmlFile = new File(pom.getBuildDir(), ClirrPlugin.CLIRR_RESULT_XML);
		if (xmlFile == null) {
			LoggerFactory.getLogger(getClass()).warn("Clirr result file '{}' not found in {}",
					ClirrPlugin.CLIRR_RESULT_XML, pom.getBuildDir());
			return 0;
		}
		LoggerFactory.getLogger(getClass()).info("Parsing {}", xmlFile.getAbsolutePath());
		ClirrXmlResultParser parser = new ClirrXmlResultParser(context);
		try {
			return parser.parse(new FileInputStream(xmlFile));
		} catch (FileNotFoundException e) {
			LoggerFactory.getLogger(getClass()).error("Unable to parse CLIRR result", e);
		}
		return 0;
	}

	private int parseTextFile(MavenPom pom, ProjectContext context, RulesManager rulesManager) {
		File txtFile = new File(pom.getBuildDir(), ClirrPlugin.CLIRR_RESULT_TXT);
		if (txtFile == null) {
			LoggerFactory.getLogger(getClass()).warn("Clirr result file '{}' not found in {}",
					ClirrPlugin.CLIRR_RESULT_XML, pom.getBuildDir());
			return 0;
		}
		LoggerFactory.getLogger(getClass()).info("Parsing {}", txtFile.getAbsolutePath());
		ClirrTxtResultParser parser = new ClirrTxtResultParser(context, rulesManager);
		try {
			return parser.parse(new FileInputStream(txtFile));
		} catch (FileNotFoundException e) {
			LoggerFactory.getLogger(getClass()).error("Unable to parse CLIRR result", e);
		}
		return 0;
	}

	// You can define dependency on another plugin to launch it before
	// collecting the results
	public Class<? extends MavenPluginHandler> dependsOnMavenPlugin(MavenPom pom) {
		return ClirrMavenPluginHandler.class;
	}

	protected boolean shouldCollectIfNoSources() {
		return true;
	}

	public boolean shouldCollectOn(MavenPom pom) {
		return true;
	}

	public boolean shouldStopOnFailure() {
		return true;
	}
}