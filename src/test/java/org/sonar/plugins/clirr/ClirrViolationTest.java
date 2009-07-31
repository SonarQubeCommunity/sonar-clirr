package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.sonar.api.resources.JavaFile;

public class ClirrViolationTest {

  @Test
  public void getAffectedClass() {
    ClirrViolation violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName());
    assertEquals(new JavaFile("org.sonar.plugins.clirr.ClirrViolationTest"), violation.getJavaFile());

    violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName() + "$InnerClass");
    assertEquals(new JavaFile("org.sonar.plugins.clirr.ClirrViolationTest"), violation.getJavaFile());
  }

}
