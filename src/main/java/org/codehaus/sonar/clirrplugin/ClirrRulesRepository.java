package org.codehaus.sonar.clirrplugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.sonar.commons.Language;
import org.sonar.commons.rules.ActiveRule;
import org.sonar.commons.rules.Rule;
import org.sonar.commons.rules.RuleFailureLevel;
import org.sonar.commons.rules.RulesProfile;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.rules.Iso9126RulesCategories;
import org.sonar.plugins.api.rules.RulesRepository;
import org.sonar.plugins.api.rules.StandardRulesXmlParser;

public class ClirrRulesRepository implements RulesRepository {

	private final Map<Integer, Rule> rules = new HashMap<Integer, Rule>(6);

	private final Properties clirrMessages = new Properties();

	public ClirrRulesRepository() {

		try {
			clirrMessages.load(new InputStreamReader(getClass().getResourceAsStream("ClirrRules.properties")));
		} catch (IOException e) {
			LoggerFactory.getLogger(getClass()).error("Unable to load clirr rule names", e);
		}

		// Scopes
		createRule("MSG_SCOPE_INCREASED", 1000);
		createRule("MSG_SCOPE_DECREASED", 1001);
		createRule("MSG_ERROR_DETERMINING_SCOPE_OLD", 1002);
		createRule("MSG_ERROR_DETERMINING_SCOPE_NEW", 1003);
		// Class/Interface gender
		createRule("MSG_GENDER_CLASS_TO_INTERFACE", 2000);
		createRule("MSG_GENDER_INTERFACE_TO_CLASS", 2001);
		// Modifiers
		createRule("MSG_MODIFIER_UNABLE_TO_DETERMINE_CLASS_SCOPE", 3000);
		createRule("MSG_MODIFIER_REMOVED_FINAL", 3001);
		createRule("MSG_MODIFIER_ADDED_FINAL_TO_EFFECTIVE_FINAL", 3002);
		createRule("MSG_MODIFIER_ADDED_FINAL", 3003);
		createRule("MSG_MODIFIER_REMOVED_ABSTRACT", 3004);
		createRule("MSG_MODIFIER_ADDED_ABSTRACT", 3005);
		// Interfaces
		createRule("MSG_IFACE_ADDED", 4000);
		createRule("MSG_IFACE_REMOVED", 4001);
		// Class Hierarchy
		createRule("MSG_ADDED_CLASS_TO_SUPERCLASSES", 5000);
		createRule("MSG_REMOVED_CLASS_FROM_SUPERCLASSES", 5001);
		// Fields
		createRule("MSG_FIELD_ADDED", 6000);
		createRule("MSG_FIELD_REMOVED", 6001);
		createRule("MSG_FIELD_NOT_CONSTANT", 6002);
		createRule("MSG_FIELD_CONSTANT_CHANGED", 6003);
		createRule("MSG_FIELD_TYPE_CHANGED", 6004);
		createRule("MSG_FIELD_NOW_NON_FINAL", 6005);
		createRule("MSG_FIELD_NOW_FINAL", 6006);
		createRule("MSG_FIELD_NOW_NON_STATIC", 6007);
		createRule("MSG_FIELD_NOW_STATIC", 6008);
		createRule("MSG_FIELD_MORE_ACCESSIBLE", 6009);
		createRule("MSG_FIELD_LESS_ACCESSIBLE", 6010);
		createRule("MSG_CONSTANT_FIELD_REMOVED", 6011);
		// Methods
		createRule("MSG_METHOD_NOW_IN_SUPERCLASS", 7000);
		createRule("MSG_METHOD_NOW_IN_INTERFACE", 7001);
		createRule("MSG_METHOD_REMOVED", 7002);
		createRule("MSG_METHOD_OVERRIDE_REMOVED", 7003);
		createRule("MSG_METHOD_ARGCOUNT_CHANGED", 7004);
		createRule("MSG_METHOD_PARAMTYPE_CHANGED", 7005);
		createRule("MSG_METHOD_RETURNTYPE_CHANGED", 7006);
		createRule("MSG_METHOD_DEPRECATED", 7007);
		createRule("MSG_METHOD_UNDEPRECATED", 7008);
		createRule("MSG_METHOD_LESS_ACCESSIBLE", 7009);
		createRule("MSG_METHOD_MORE_ACCESSIBLE", 7010);
		createRule("MSG_METHOD_ADDED", 7011);
		createRule("MSG_METHOD_ADDED_TO_INTERFACE", 7012);
		createRule("MSG_ABSTRACT_METHOD_ADDED", 7013);
		createRule("MSG_METHOD_NOW_FINAL", 7014);
		createRule("MSG_METHOD_NOW_NONFINAL", 7015);
		// Class Format Version
		createRule("MSG_CLASS_FORMAT_VERSION_INCREASED", 10000);
		createRule("MSG_CLASS_FORMAT_VERSION_DECREASED", 10001);
	}

	private void createRule(String name, int id) {
		String key = "clirr" + id;
		String label = trimLabel(name, id);
		String description = createDescription(name, id);
		String configKey = "clirrcfg";
		String pluginName = ClirrPlugin.CLIRR_PLUGIN_NAME;
		Rule rule = new Rule(label, key, configKey, Iso9126RulesCategories.PORTABILITY, pluginName, description);
		rules.put(id, rule);
	}

	private String createDescription(String name, int id) {
		String fullName = clirrMessages.getProperty("m" + id, name);
		return String.format("Clirr Check #%d: %s", id, fullName);
	}

	private String trimLabel(String name, int id) {
		return StringUtils.abbreviate("Compatibility - " + clirrMessages.getProperty("m" + id, name), 64);
	}

	public List<Rule> getInitialReferential() {
		return new ArrayList<Rule>(rules.values());
	}

	public Language getLanguage() {
		return new Java();
	}

	public List<RulesProfile> getProvidedProfiles() {
		List<RulesProfile> profiles = new ArrayList<RulesProfile>();

		RulesProfile profile = new RulesProfile();
		profile.setName(RulesProfile.SONAR_WAY_NAME);
		profile.setLanguage(Java.KEY);
		profile.setActiveRules(populateActiveRules());

		profiles.add(profile);
		return profiles;
	}

	private List<ActiveRule> populateActiveRules() {
		final List<ActiveRule> activeRules = new ArrayList<ActiveRule>();
		List<Rule> initialReferential = getInitialReferential();
		for (Rule rule : initialReferential) {
			ActiveRule activeRule = new ActiveRule(null, rule, RuleFailureLevel.ERROR);
			activeRules.add(activeRule);
		}
		return activeRules;
	}

	public List<Rule> parseReferential(String fileContent) {
		return new StandardRulesXmlParser().parse(fileContent);
	}

	public Rule findByClirrId(int id) {
		return rules.get(id);
	}
}
