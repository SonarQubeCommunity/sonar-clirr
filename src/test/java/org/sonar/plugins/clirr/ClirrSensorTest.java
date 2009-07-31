package org.sonar.plugins.clirr;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.Violation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class ClirrSensorTest {

  private Map<JavaFile, List<ClirrViolation>> violationsByFile;
  RulesProfile rulesProfile = mock(RulesProfile.class);
  ClirrSensor sensor;
  SensorContext context = mock(SensorContext.class);

  @Before
  public void setup() throws IOException {
    violationsByFile = new ClirrTxtResultParser().parseToGetViolationsByResource(ClirrTxtResultParserTest.class
        .getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
    sensor = new ClirrSensor(rulesProfile, new ClirrMavenPluginHandler(), new ClirrRulesRepository());
  }

  @Test
  public void saveViolationsAndMeasuresWithoutActiveRules() {
    JavaFile JavaFile = new JavaFile("org.sonar.plugins.api.AbstractLanguage");
    List<ClirrViolation> violations = violationsByFile.get(JavaFile);
    sensor.saveViolationsAndMeasures(context, JavaFile, violations);
    verify(context).saveMeasure(JavaFile, ClirrMetrics.TOTAL_API_CHANGES, (double) 0);
    verify(context, never()).saveViolation((Violation) anyObject());
  }

  @Test
  public void saveViolationsAndMeasuresWithActiveRules() {
    JavaFile JavaFile = new JavaFile("org.sonar.plugins.api.AbstractLanguage");
    ActiveRule activeRule = new ActiveRule();
    activeRule.setPriority(RulePriority.CRITICAL);
    when(rulesProfile.getActiveRule(anyString(), anyString())).thenReturn(activeRule);
    List<ClirrViolation> violations = violationsByFile.get(JavaFile);
    sensor.saveViolationsAndMeasures(context, JavaFile, violations);
    verify(context).saveMeasure(JavaFile, ClirrMetrics.TOTAL_API_CHANGES, (double) 2);
    verify(context).saveMeasure(JavaFile, ClirrMetrics.API_BREAKS, (double) 1);
    verify(context).saveMeasure(JavaFile, ClirrMetrics.API_BEHAVIOR_CHANGES, (double) 1);

    // TODO check violation fields
    verify(context, times(2)).saveViolation((Violation) anyObject());
  }
}
