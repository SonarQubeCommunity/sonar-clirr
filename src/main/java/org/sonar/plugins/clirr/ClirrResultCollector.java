package org.sonar.plugins.clirr;

import org.slf4j.LoggerFactory;
import org.sonar.plugins.api.maven.AbstractJavaMavenCollector;
import org.sonar.plugins.api.maven.MavenPluginHandler;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.model.MavenPom;
import org.sonar.plugins.api.rules.RulesManager;

public final class ClirrResultCollector extends AbstractJavaMavenCollector {

	private final RulesManager rulesManager;

	// XML disabled - it does not contain MessageId :(
	private static final boolean USE_XML = false;

	private ClirrParser clirrParser;

	public ClirrResultCollector(RulesManager rulesManager) {
		this.rulesManager = rulesManager;
	}

	public void collect(MavenPom pom, ProjectContext context) {
		try {
			ClirrParser parser = clirrParser != null ? clirrParser : createParser(pom, context);
			parser.parse();
			context.addMeasure(ClirrMetrics.APIBREAKS, Double.valueOf(parser.getIncompatibilities()));
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error("Clirr result not parsed", e);
		}
	}

	protected ClirrParser createParser(MavenPom pom, ProjectContext context) {
		if (USE_XML) {
			return new ClirrXmlResultParser(pom, context, rulesManager);
		}
		return new ClirrTxtResultParser(pom, context, rulesManager);
	}

	public Class<? extends MavenPluginHandler> dependsOnMavenPlugin(MavenPom pom) {
		return ClirrMavenPluginHandler.class;
	}

	@Override
	protected boolean shouldCollectIfNoSources() {
		// Clirr works on binary classes
		return true;
	}

	public void setParser(ClirrParser clirrParser) {
		this.clirrParser = clirrParser;
	}

}