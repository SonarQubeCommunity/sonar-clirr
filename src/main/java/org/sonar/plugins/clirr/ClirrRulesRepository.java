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

public final class ClirrRulesRepository implements RulesRepository<Java> {

	private final Rule binaryIncompatibilityRule;
	private final Rule runtimeImpactRule;
	private final Rule runtimeCompatibilityRule;
	private final List<Rule> rules = new ArrayList<Rule>();

	public ClirrRulesRepository() {
		binaryIncompatibilityRule = new Rule(
				"API Change breaks the backward binary compatibility",
				"clirr-binary",
				ClirrPlugin.CLIRR_PLUGIN_KEY,
				new RulesCategory("Portability"),
				ClirrPlugin.CLIRR_PLUGIN_NAME,
				"Clirr reports this violation for cases where it is possible to get a run-time failure. Whether one actually occurs can depend upon the way the library is called, ie changes reported as an error may in fact work when used as long as the patterns of use of the library do not trigger the failure situation.");
		binaryIncompatibilityRule.setPriority(RulePriority.CRITICAL);
		rules.add(binaryIncompatibilityRule);
		runtimeImpactRule = new Rule(
				"API Change might change runtime expected behavior",
				"clirr-behavior",
				ClirrPlugin.CLIRR_PLUGIN_KEY,
				new RulesCategory("Portability"),
				ClirrPlugin.CLIRR_PLUGIN_NAME,
				"Clirr reports this violation for situations where no link or runtime exception will occur, but where the application may behave unexpectedly due to the changes that have occurred.");
		runtimeImpactRule.setPriority(RulePriority.MAJOR);
		rules.add(runtimeImpactRule);
		runtimeCompatibilityRule = new Rule(
				"API Change adds new feature without breaking anything",
				"clirr-newapi",
				ClirrPlugin.CLIRR_PLUGIN_KEY,
				new RulesCategory("Portability"),
				ClirrPlugin.CLIRR_PLUGIN_NAME,
				"Clirr reports this information messages when new features have been added without breaking backward compatibility in any way.");
		runtimeCompatibilityRule.setPriority(RulePriority.INFO);
		rules.add(runtimeCompatibilityRule);
	}

	public Rule getRuleFromClirrViolation(ClirrViolation violation) {
		if ("ERROR".equals(violation.getSeverity())) {
			return binaryIncompatibilityRule;
		} else if ("WARNING".equals(violation.getSeverity())) {
			return runtimeImpactRule;
		} else if ("INFO".equals(violation.getSeverity())) {
			return runtimeCompatibilityRule;
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
