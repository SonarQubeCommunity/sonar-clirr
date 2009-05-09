package org.sonar.plugins.clirr;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.sonar.plugins.api.maven.MavenTestCase;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.model.MavenPom;
import org.sonar.plugins.api.rules.RulesManager;

public class ClirrResultCollectorTest {

	@Test
	public void noClirrResultMeansNoApiBreaksMeasure() {
		ProjectContext context = mock(ProjectContext.class);
		RulesManager rulesManager = mock(RulesManager.class);
		MavenPom pom = MavenTestCase.mockPom("jar");
		new ClirrResultCollector(rulesManager).collect(pom, context);
		verify(context, atLeastOnce()).addMeasure(eq(ClirrMetrics.APIBREAKS), eq(0.0d));
	}

	@Test
	public void someClirrResults() {
		ProjectContext context = mock(ProjectContext.class);
		RulesManager rulesManager = mock(RulesManager.class);
		ClirrParser clirrParser = mock(ClirrParser.class);
		when(clirrParser.getIncompatibilities()).thenReturn(1);

		MavenPom pom = MavenTestCase.mockPom("jar");
		ClirrResultCollector collector = new ClirrResultCollector(rulesManager);
		collector.setParser(clirrParser);
		collector.collect(pom, context);
		verify(context, atLeastOnce()).addMeasure(eq(ClirrMetrics.APIBREAKS), eq(1.0d));
	}

}
