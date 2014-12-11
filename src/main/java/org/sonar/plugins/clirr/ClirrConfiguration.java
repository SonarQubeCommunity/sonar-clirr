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

import org.sonar.api.BatchComponent;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;

import javax.annotation.CheckForNull;

public class ClirrConfiguration implements BatchComponent {

  private ActiveRules activeRules;
  private Settings settings;

  public ClirrConfiguration(ActiveRules activeRules, Settings settings) {
    this.activeRules = activeRules;
    this.settings = settings;
  }

  boolean isActive() {
    return getReportPath() != null
      && (isApiBreakActive() || isApiBehaviorChangeActive() || isNewApiActive());
  }

  boolean isApiBreakActive() {
    return isActive(ClirrConstants.RULE_API_BREAK);
  }

  boolean isApiBehaviorChangeActive() {
    return isActive(ClirrConstants.RULE_API_BEHAVIOR_CHANGE);
  }

  boolean isNewApiActive() {
    return isActive(ClirrConstants.RULE_NEW_API);
  }

  @CheckForNull
  String getReportPath() {
    return settings.getString(ClirrConstants.REPORT_PATH);
  }

  boolean isActive(RuleKey key) {
    return activeRules.find(key) != null;
  }
}
