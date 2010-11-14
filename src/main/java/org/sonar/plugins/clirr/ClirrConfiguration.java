/*
 * Sonar Clirr Plugin
 * Copyright (C) 2009 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;

public class ClirrConfiguration implements BatchExtension {

  private RulesProfile profile;
  private Configuration configuration;

  public ClirrConfiguration(RulesProfile profile, Configuration configuration) {
    this.profile = profile;
    this.configuration = configuration;
  }

  public boolean isActive() {
    return configuration.getBoolean(ClirrConstants.API_PROPERTY, false)
        && (isApiBreakActive() || isApiBehaviorChangeActive() || isNewApiActive());
  }

  public String getComparisonVersion() {
    return configuration.getString(ClirrConstants.COMPARISON_VERSION_PROPERTY);
  }

  public boolean hasComparisonVersion() {
    return StringUtils.isNotBlank(getComparisonVersion());
  }

  public boolean isApiBreakActive() {
    return getActiveRule(ClirrConstants.RULE_API_BREAK) != null;
  }

  public boolean isApiBehaviorChangeActive() {
    return getActiveRule(ClirrConstants.RULE_API_BEHAVIOR_CHANGE) != null;
  }

  public boolean isNewApiActive() {
    return getActiveRule(ClirrConstants.RULE_NEW_API) != null;
  }

  public ActiveRule getActiveRule(String key) {
    return profile.getActiveRule(ClirrConstants.PLUGIN_KEY, key);
  }
}
