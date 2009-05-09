package org.sonar.plugins.clirr;

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

public final class ClirrRulesRepository implements RulesRepository {

	private static final String ERROR_CANNOT_LOAD_RULE_DESCRIPTIONS = "Unable to load clirr rule names";

	private static final int MESSAGE_MAX_LENGTH = 64;

	private final Map<Integer, Rule> rules = new HashMap<Integer, Rule>(ClirrMessages.values().length);

	private final Properties clirrMessages = new Properties();

	public ClirrRulesRepository() {
		readMessagesAndDescriptions();
		createAndRegisterRules();
	}

	private void readMessagesAndDescriptions() {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(getClass().getResourceAsStream("ClirrRules.properties"));
			clirrMessages.load(reader);
		} catch (IOException e) {
			LoggerFactory.getLogger(getClass()).error(ERROR_CANNOT_LOAD_RULE_DESCRIPTIONS, e);
		} finally {
			ClirrPlugin.safeClose(reader);
		}
	}

	private void createAndRegisterRules() {
		for (ClirrMessages messages : ClirrMessages.values()) {
			rules.put(messages.getId(), createRule(messages.name(), messages.getId()));
		}
	}

	private Rule createRule(String name, int id) {
		String key = "clirr" + id;
		String label = trimLabel(name, id);
		String description = createDescription(name, id);
		String configKey = "clirrcfg";
		String pluginName = ClirrPlugin.CLIRR_PLUGIN_NAME;
		return new Rule(label, key, configKey, Iso9126RulesCategories.PORTABILITY, pluginName, description);
	}

	private String createDescription(String name, int id) {
		String fullName = clirrMessages.getProperty("m" + id, name);
		return String.format("Clirr Check #%d: %s", id, fullName);
	}

	private String trimLabel(String name, int id) {
		String message = "Compatibility - " + clirrMessages.getProperty("m" + id, name);
		return StringUtils.abbreviate(message, MESSAGE_MAX_LENGTH);
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
			activeRules.add(new ActiveRule(null, rule, RuleFailureLevel.ERROR));
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
