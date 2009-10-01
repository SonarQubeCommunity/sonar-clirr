package org.sonar.plugins.clirr;

import org.sonar.api.Extension;
import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;

import java.util.ArrayList;
import java.util.List;

@Properties({
    @Property(key = ClirrPlugin.CLIRR_KEY_COMPARISON_VERSION, name = "Reference version", description = "By default, Clirr compares the current code against the latest released version. Use this parameter to compare your code against a particular version.", project = true, module = true, global = false),
    @Property(key = ClirrPlugin.CLIRR_KEY_EXECUTE, defaultValue = "false", name = "Activation", description = "Clirr is not activated by default. You need to explicitly activate it on any desired projects/modules.", project = true, module = true, global = false)})
public final class ClirrPlugin implements Plugin {

  public static final String CLIRR_PLUGIN_KEY = "clirr";
  public static final String CLIRR_PLUGIN_NAME = "Clirr";
  public static final String CLIRR_RESULT_TXT = "clirr-result.txt";
  public static final String CLIRR_KEY_COMPARISON_VERSION = "sonar.clirr.comparisonVersion";
  public static final String CLIRR_KEY_EXECUTE = "sonar.clirr.execute";

  // This description will be displayed in the Configuration > Settings web
  // page
  public String getDescription() {
    return "The Clirr plugin measures API breaks.";
  }

  // This is where you're going to declare all your Sonar extensions
  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();
    list.add(ClirrSensor.class);
    list.add(ClirrRulesRepository.class);
    list.add(ClirrMavenPluginHandler.class);
    list.add(ClirrDecorator.class);
    list.add(ClirrMetrics.class);
    list.add(ClirrWidget.class);
    return list;
  }

  // The key which uniquely identifies your plugin among all others Sonar
  // plugins
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