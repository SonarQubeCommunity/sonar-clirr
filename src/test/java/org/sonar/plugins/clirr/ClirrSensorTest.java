package org.sonar.plugins.clirr;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.MavenPluginExecutor;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.JavaClass;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;

public class ClirrSensorTest {

	private Map<JavaClass, List<ClirrViolation>> violationsByFile;
	RulesProfile rulesProfile = mock(RulesProfile.class);
	ClirrSensor sensor;
	SensorContext context = mock(SensorContext.class);

	@Before
	public void setup() throws IOException {
		violationsByFile = new ClirrTxtResultParser().parseToGetViolationsByResource(ClirrTxtResultParserTest.class
				.getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
		sensor = new ClirrSensor(rulesProfile, mock(MavenPluginExecutor.class), new ClirrMavenPluginHandler(),
				new ClirrRulesRepository());
	}

	@Test
	public void saveViolationsAndMeasuresWithoutActiveRules() {
		JavaClass javaClass = new JavaClass("org.sonar.plugins.api.AbstractLanguage");
		List<ClirrViolation> violations = violationsByFile.get(javaClass);
		sensor.saveViolationsAndMeasures(context, javaClass, violations);
		verify(context).saveMeasure(javaClass, ClirrMetrics.TOTAL_API_CHANGES, (double) 0);
		verify(context, never()).saveViolation((JavaClass) anyObject(), (Rule) anyObject(), anyString(),
				(RulePriority) anyObject(), (Integer) anyObject());
	}
	
	@Test
	public void saveViolationsAndMeasuresWithActiveRules() {
		JavaClass javaClass = new JavaClass("org.sonar.plugins.api.AbstractLanguage");
		ActiveRule activeRule = new ActiveRule();
		activeRule.setPriority(RulePriority.CRITICAL);
		when(rulesProfile.getActiveRule(anyString(), anyString())).thenReturn(activeRule);
		List<ClirrViolation> violations = violationsByFile.get(javaClass);
		sensor.saveViolationsAndMeasures(context, javaClass, violations);
		verify(context).saveMeasure(javaClass, ClirrMetrics.TOTAL_API_CHANGES, (double) 2);
		verify(context).saveMeasure(javaClass, ClirrMetrics.API_BREAKS, (double) 1);
		verify(context).saveMeasure(javaClass, ClirrMetrics.API_BEHAVIOR_CHANGES, (double) 1);
		verify(context, times(2)).saveViolation((JavaClass) anyObject(), (Rule) anyObject(), anyString(),
				(RulePriority) anyObject(), (Integer) anyObject());
	}
}
