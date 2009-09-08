package org.sonar.plugins.clirr;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.Violation;
import org.sonar.api.test.IsViolation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class ClirrSensorTest {

  private List<ClirrViolation> violations;
  private ClirrSensor sensor;
  private RulesProfile rulesProfile;
  private SensorContext context;
  private Project project;

  @Before
  public void setup() throws IOException {
    violations = new ClirrTxtResultParser().parse(ClirrTxtResultParserTest.class
        .getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
    rulesProfile = mock(RulesProfile.class);
    context = mock(SensorContext.class);
    project = mock(Project.class);
    sensor = new ClirrSensor(rulesProfile, new ClirrMavenPluginHandler(), new ClirrRulesRepository());
  }

  @Test
  public void doNotSaveViolationsIfNotActiveRule() {
    sensor.saveViolations(violations, context, project);
    verify(context, never()).saveViolation((Violation) anyObject());
  }

  @Test
  public void saveViolationsOnDeletedResources() {
    ActiveRule activeRule = new ActiveRule();
    activeRule.setPriority(RulePriority.CRITICAL);
    when(rulesProfile.getActiveRule(anyString(), anyString())).thenReturn(activeRule);

    sensor.saveViolations(violations, context, project);

    verify(context, times(106)).saveViolation(argThat(new IsViolation(null, null, project, null)));
  }
}
