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

import org.sonar.api.resources.Java;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RuleRepository;

import java.util.Arrays;
import java.util.List;


public final class ClirrRulesRepository extends RuleRepository {

  public ClirrRulesRepository() {
    super(ClirrConstants.PLUGIN_KEY, Java.KEY);
    setName(ClirrConstants.PLUGIN_NAME);
  }

  @Override
  public List<Rule> createRules() {
    Rule apiBreakRule = Rule.create()
        .setKey(ClirrConstants.RULE_API_BREAK)
        .setName("API Change breaks the backward binary compatibility")
        .setSeverity(RulePriority.CRITICAL)
        .setDescription("Clirr reports this violation for cases where it is possible to get a run-time failure." +
            " Whether one actually occurs can depend upon the way the library is called, ie changes reported as an error may" +
            " in fact work when used as long as the patterns of use of the library do not trigger the failure situation.");

    Rule apiBehaviorChangeRule = Rule.create()
        .setKey(ClirrConstants.RULE_API_BEHAVIOR_CHANGE)
        .setName("API Change might change runtime expected behavior")
        .setSeverity(RulePriority.MAJOR)
        .setDescription("Clirr reports this violation for situations where no link or runtime exception will occur," +
            " but where the application may behave unexpectedly due to the changes that have occurred.");

    Rule newApiRule = Rule.create()
        .setKey(ClirrConstants.RULE_NEW_API)
        .setName("API Change adds new feature without breaking anything")
        .setSeverity(RulePriority.INFO)
        .setDescription("Clirr reports this information messages when new features have been added without breaking backward" +
            " compatibility in any way.");

    return Arrays.asList(apiBreakRule, apiBehaviorChangeRule, newApiRule);
  }

}
