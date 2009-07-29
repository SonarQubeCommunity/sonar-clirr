package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.sonar.api.rules.Rule;

public class ClirrRulesRepositoryTest {

	@Test
	public void getInitialReferential() {
		List<Rule> rules = new ClirrRulesRepository().getInitialReferential();
		assertEquals(3, rules.size());
	}

	@Test
	public void getRuleFromClirrViolation() {
		ClirrViolation error = new ClirrViolation("ERROR", "", "", "");
		assertEquals("clirr-api-break", new ClirrRulesRepository().getRuleFromClirrViolation(error).getKey());
		ClirrViolation warning = new ClirrViolation("WARNING", "", "", "");
		assertEquals("clirr-api-behavior-change", new ClirrRulesRepository().getRuleFromClirrViolation(warning).getKey());
		ClirrViolation info = new ClirrViolation("INFO", "", "", "");
		assertEquals("clirr-new-api", new ClirrRulesRepository().getRuleFromClirrViolation(info).getKey());
	}

}
