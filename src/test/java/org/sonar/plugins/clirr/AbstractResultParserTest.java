package org.sonar.plugins.clirr;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sonar.commons.rules.RuleFailureLevel;

public class AbstractResultParserTest {

	@Test
	public void testMapClirrSeverity() {
		assertTrue(AbstractResultParser.mapClirrSeverity("info") == RuleFailureLevel.INFO);
		assertTrue(AbstractResultParser.mapClirrSeverity("warning") == RuleFailureLevel.WARNING);
		assertTrue(AbstractResultParser.mapClirrSeverity("error") == RuleFailureLevel.ERROR);
		assertTrue(AbstractResultParser.mapClirrSeverity("anything else") == RuleFailureLevel.INFO);
	}

}
