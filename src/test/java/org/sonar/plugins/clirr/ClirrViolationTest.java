package org.sonar.plugins.clirr;

import org.junit.Test;
import static org.junit.Assert.*;


public class ClirrViolationTest {
	
	@Test
	public void getAffectedClass(){
		ClirrViolation violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName());
		assertEquals("org.sonar.plugins.clirr.ClirrViolationTest", violation.getAffectedClass());
		
		violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName() + "$InnerClass");
		assertEquals("org.sonar.plugins.clirr.ClirrViolationTest", violation.getAffectedClass());
	}
	

}
