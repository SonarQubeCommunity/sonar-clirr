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
import org.sonar.api.BatchComponent;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;

public class ClirrConfiguration implements BatchComponent {

  private ActiveRules activeRules;
  private Settings settings;

  public ClirrConfiguration(ActiveRules activeRules, Settings settings) {
    this.activeRules = activeRules;
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

  org.sonar.api.batch.rule.ActiveRule getActiveRule(RuleKey key) {
    return activeRules.find(key);
  }
}
