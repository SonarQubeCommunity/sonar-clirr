package org.sonar.plugins.clirr;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RulesCategory;
import org.sonar.api.rules.RulesRepository;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.clirr.ClirrViolation.Type;

public final class ClirrRulesRepository implements RulesRepository<Java> {

	private final Rule apiBreakRule;
	private final Rule apiBehaviorChangeRule;
	private final Rule newApiRule;
	private final List<Rule> rules = new ArrayList<Rule>();

	public ClirrRulesRepository() {
		apiBreakRule = new Rule(
				"API Change breaks the backward binary compatibility",
				"clirr-api-break",
				ClirrPlugin.CLIRR_PLUGIN_KEY,
				new RulesCategory("Portability"),
				ClirrPlugin.CLIRR_PLUGIN_NAME,
				"Clirr reports this violation for cases where it is possible to get a run-time failure. Whether one actually occurs can depend upon the way the library is called, ie changes reported as an error may in fact work when used as long as the patterns of use of the library do not trigger the failure situation.");
		apiBreakRule.setPriority(RulePriority.CRITICAL);
		rules.add(apiBreakRule);
		apiBehaviorChangeRule = new Rule(
				"API Change might change runtime expected behavior",
				"clirr-api-behavior-change",
				ClirrPlugin.CLIRR_PLUGIN_KEY,
				new RulesCategory("Portability"),
				ClirrPlugin.CLIRR_PLUGIN_NAME,
				"Clirr reports this violation for situations where no link or runtime exception will occur, but where the application may behave unexpectedly due to the changes that have occurred.");
		apiBehaviorChangeRule.setPriority(RulePriority.MAJOR);
		rules.add(apiBehaviorChangeRule);
		newApiRule = new Rule(
				"API Change adds new feature without breaking anything",
				"clirr-new-api",
				ClirrPlugin.CLIRR_PLUGIN_KEY,
				new RulesCategory("Portability"),
				ClirrPlugin.CLIRR_PLUGIN_NAME,
				"Clirr reports this information messages when new features have been added without breaking backward compatibility in any way.");
		newApiRule.setPriority(RulePriority.INFO);
		rules.add(newApiRule);
	}

	public Rule getRuleFromClirrViolation(ClirrViolation violation) {
		if (violation.getType() == Type.BREAK) {
			return apiBreakRule;
		} else if (violation.getType() == Type.BEHAVIOR_CHANGE) {
			return apiBehaviorChangeRule;
		} else if (violation.getType() == Type.NEW_API) {
			return newApiRule;
		} else {
			throw new SonarException("There is no Clirr Sonar rule associated to '" + violation.getSeverity()
					+ "' level");
		}
	}

	public List<Rule> getInitialReferential() {
		return rules;
	}

	public Java getLanguage() {
		return new Java();
	}

	public List<RulesProfile> getProvidedProfiles() {
		return new ArrayList<RulesProfile>();
	}

	public List<Rule> parseReferential(String fileContent) {
		return new ArrayList<Rule>();
	}

}
