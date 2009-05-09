package org.sonar.plugins.clirr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ClirrRulesRepositoryTest {
	@Test
	public void staticClirrRulesObeySonarContract() throws Exception {
		ClirrRulesRepository repository = new ClirrRulesRepository();
		List<org.sonar.commons.rules.Rule> rules = repository.getInitialReferential();
		assertTrue(rules.size() > 0);
		for (org.sonar.commons.rules.Rule rule : rules) {
			assertNotNull(rule.getKey());
			assertNotNull(rule.getDescription());
			assertNotNull(rule.getConfigKey());
			assertNotNull(rule.getName());
		}
	}
}
