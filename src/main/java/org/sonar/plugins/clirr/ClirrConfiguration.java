/*
 * SonarQube Clirr Plugin
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

package org.sonar.plugins.clirr;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;

public class ClirrConfiguration implements BatchExtension {

  private RulesProfile profile;
  private Settings settings;

  public ClirrConfiguration(RulesProfile profile, Settings settings) {
    this.profile = profile;
    this.settings = settings;
  }

  boolean isActive() {
    return settings.getBoolean(ClirrConstants.API_PROPERTY)
      && (isApiBreakActive() || isApiBehaviorChangeActive() || isNewApiActive());
  }

  String getComparisonVersion() {
    return settings.getString(ClirrConstants.COMPARISON_VERSION_PROPERTY);
  }

  boolean hasComparisonVersion() {
    return StringUtils.isNotBlank(getComparisonVersion());
  }

  boolean isApiBreakActive() {
    return getActiveRule(ClirrConstants.RULE_API_BREAK) != null;
  }

  boolean isApiBehaviorChangeActive() {
    return getActiveRule(ClirrConstants.RULE_API_BEHAVIOR_CHANGE) != null;
  }

  boolean isNewApiActive() {
    return getActiveRule(ClirrConstants.RULE_NEW_API) != null;
  }

  ActiveRule getActiveRule(String key) {
    return profile.getActiveRule(ClirrConstants.PLUGIN_KEY, key);
  }
}
