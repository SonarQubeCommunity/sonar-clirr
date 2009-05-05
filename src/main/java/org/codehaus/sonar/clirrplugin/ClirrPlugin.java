package org.codehaus.sonar.clirrplugin;

import java.util.ArrayList;
import java.util.List;

import org.sonar.plugins.api.EditableProperties;
import org.sonar.plugins.api.EditableProperty;
import org.sonar.plugins.api.Extension;

@EditableProperties( { @EditableProperty(key = ClirrPlugin.CONFIG_MIN_SEVERITY, defaultValue = "warning", name = "Minimum severity to display for API breaks", description = "One of INFO, WARNING (default) or ERROR.") })
public class ClirrPlugin implements org.sonar.plugins.api.Plugin {

	public static final String CLIRR_PLUGIN_KEY = "clirr";
	public static final String CLIRR_PLUGIN_NAME = "CLIRR";
	public static final String CLIRR_RESULT_XML = "clirr-result.xml";
	public static final String CLIRR_RESULT_TXT = "clirr-result.txt";
	public static final String CONFIG_MIN_SEVERITY = "sonar.clirr.config.minSeverity";

	// This description will be displayed in the Configuration > Settings web page
	public String getDescription() {
		return "The CLIRR plugin measures API breaks. Code breaks are categorized by severity: INFO, WARNING and ERROR.";
	}

	// This is where you're going to declare all your Sonar extensions
	public List<Class<? extends Extension>> getExtensions() {
		List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();
		list.add(ClirrMetrics.class);
		list.add(ClirrResultCollector.class);
		list.add(ClirrJob.class);
		list.add(ClirrRulesRepository.class);
		return list;
	}

	// The key which uniquely identifies your plugin among all others Sonar plugins
	public String getKey() {
		return CLIRR_PLUGIN_KEY;
	}

	public String getName() {
		return CLIRR_PLUGIN_NAME;
	}

	@Override
	public String toString() {
		return getKey();
	}
}