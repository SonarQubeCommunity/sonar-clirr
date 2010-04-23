/*
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.clirr;

import org.sonar.api.Extension;
import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;

import java.util.ArrayList;
import java.util.List;

@Properties({
    @Property(key = ClirrConstants.COMPARISON_VERSION_PROPERTY, name = "Reference version", description = "By default, Clirr compares the current code against the latest released version. Use this parameter to compare your code against a particular version.", project = true, module = true, global = false),
    @Property(key = ClirrConstants.API_PROPERTY, defaultValue = "false", name = "API", description = "Only API projects are checked. Set to true if this project is considered as an API. Default is false.", project = true, module = true, global = false)
})
public final class ClirrPlugin implements Plugin {

  // This is where you're going to declare all your Sonar extensions
  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();
    list.add(ClirrSensor.class);
    list.add(ClirrRulesRepository.class);
    list.add(ClirrMavenPluginHandler.class);
    list.add(ClirrDecorator.class);
    list.add(ClirrMetrics.class);
    list.add(ClirrWidget.class);
    list.add(ClirrPage.class);
    list.add(ClirrConfiguration.class);
    return list;
  }

  // The key which uniquely identifies your plugin among all others Sonar
  // plugins
  public String getKey() {
    return ClirrConstants.PLUGIN_KEY;
  }

  public String getName() {
    return ClirrConstants.PLUGIN_NAME;
  }

  public String getDescription() {
    return "Clirr checks Java libraries for binary and source compatibility with older releases. Basically Clirr dumps out a list of changes in the public api. It prevents accidental introduction of binary or source compatibility problems between two versions.";
  }

  @Override
  public String toString() {
    return getKey();
  }
}