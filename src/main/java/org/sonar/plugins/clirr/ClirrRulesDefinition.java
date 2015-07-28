/*
 * SonarQube Clirr Plugin
 * Copyright (C) 2009 SonarSource
 * sonarqube@googlegroups.com
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

import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

public final class ClirrRulesDefinition implements RulesDefinition {

  @Override
  public void define(Context context) {
    NewRepository newRepository = context.createRepository(ClirrConstants.PLUGIN_KEY, "java")
      .setName(ClirrConstants.PLUGIN_NAME);

    newRepository.createRule(ClirrConstants.RULE_API_BREAK.rule())
      .setName("API Change breaks the backward binary compatibility")
      .setSeverity(Severity.CRITICAL)
      .setHtmlDescription("Clirr reports this violation for cases where it is possible to get a run-time failure." +
        " Whether one actually occurs can depend upon the way the library is called, ie changes reported as an error may" +
        " in fact work when used as long as the patterns of use of the library do not trigger the failure situation.");

    newRepository.createRule(ClirrConstants.RULE_API_BEHAVIOR_CHANGE.rule())
      .setName("API Change might change runtime expected behavior")
      .setSeverity(Severity.MAJOR)
      .setHtmlDescription("Clirr reports this violation for situations where no link or runtime exception will occur," +
        " but where the application may behave unexpectedly due to the changes that have occurred.");

    newRepository.createRule(ClirrConstants.RULE_NEW_API.rule())
      .setName("API Change adds new feature without breaking anything")
      .setSeverity(Severity.INFO)
      .setHtmlDescription("Clirr reports this information messages when new features have been added without breaking backward" +
        " compatibility in any way.");

    newRepository.done();

  }

}
