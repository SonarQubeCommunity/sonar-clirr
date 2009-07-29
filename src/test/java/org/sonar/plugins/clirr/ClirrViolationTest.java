package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sonar.api.resources.JavaClass;

public class ClirrViolationTest {

	@Test
	public void getAffectedClass() {
		ClirrViolation violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName());
		assertEquals(new JavaClass("org.sonar.plugins.clirr.ClirrViolationTest"), violation.getJavaClass());

		violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName() + "$InnerClass");
		assertEquals(new JavaClass("org.sonar.plugins.clirr.ClirrViolationTest"), violation.getJavaClass());
	}

}
