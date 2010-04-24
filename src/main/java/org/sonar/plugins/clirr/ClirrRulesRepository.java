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

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Java;
import org.sonar.api.rules.Iso9126RulesCategories;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RulesRepository;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.clirr.ClirrViolation.Type;

import java.util.ArrayList;
import java.util.List;

public final class ClirrRulesRepository implements RulesRepository<Java> {

  private final Rule apiBreakRule;
  private final Rule apiBehaviorChangeRule;
  private final Rule newApiRule;
  private final List<Rule> rules = new ArrayList<Rule>();

  public ClirrRulesRepository() {
    apiBreakRule = new Rule(
        ClirrConstants.PLUGIN_KEY,
        ClirrConstants.RULE_API_BREAK,
        "API Change breaks the backward binary compatibility",
        Iso9126RulesCategories.PORTABILITY,
        RulePriority.CRITICAL);
    apiBreakRule.setDescription("Clirr reports this violation for cases where it is possible to get a run-time failure." +
        " Whether one actually occurs can depend upon the way the library is called, ie changes reported as an error may" +
        " in fact work when used as long as the patterns of use of the library do not trigger the failure situation.");
    apiBreakRule.setConfigKey(apiBreakRule.getKey());
    rules.add(apiBreakRule);

    apiBehaviorChangeRule = new Rule(
        ClirrConstants.PLUGIN_KEY,
        ClirrConstants.RULE_API_BEHAVIOR_CHANGE,
        "API Change might change runtime expected behavior",
        Iso9126RulesCategories.PORTABILITY,
        RulePriority.MAJOR);
    apiBehaviorChangeRule.setDescription("Clirr reports this violation for situations where no link or runtime exception will occur," +
        " but where the application may behave unexpectedly due to the changes that have occurred.");
    apiBehaviorChangeRule.setConfigKey(apiBehaviorChangeRule.getKey());
    rules.add(apiBehaviorChangeRule);

    newApiRule = new Rule(
        ClirrConstants.PLUGIN_KEY,
        ClirrConstants.RULE_NEW_API,
        "API Change adds new feature without breaking anything",
        Iso9126RulesCategories.PORTABILITY, RulePriority.INFO);
    newApiRule.setDescription("Clirr reports this information messages when new features have been added without breaking backward" +
        " compatibility in any way.");
    newApiRule.setConfigKey(newApiRule.getKey());
    rules.add(newApiRule);
  }

  public Rule getRuleFromClirrViolation(ClirrViolation violation) {
    if (violation.getType() == Type.BREAK) {
      return apiBreakRule;
    }
    if (violation.getType() == Type.BEHAVIOR_CHANGE) {
      return apiBehaviorChangeRule;
    }
    if (violation.getType() == Type.NEW_API) {
      return newApiRule;
    }
    throw new SonarException("There is no Clirr Sonar rule associated to '" + violation.getSeverity() + "' level");
  }

  public List<Rule> getInitialReferential() {
    return rules;
  }

  public Java getLanguage() {
    return Java.INSTANCE;
  }

  public List<RulesProfile> getProvidedProfiles() {
    return new ArrayList<RulesProfile>();
  }

  public List<Rule> parseReferential(String fileContent) {
    return new ArrayList<Rule>();
  }

  public Rule getApiBreakRule() {
    return apiBreakRule;
  }

  public Rule getApiBehaviorChangeRule() {
    return apiBehaviorChangeRule;
  }

  public Rule getNewApiRule() {
    return newApiRule;
  }
}
