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

import org.junit.Test;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.config.Settings;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClirrConfigurationTest {

  Settings settings = new Settings();
  ActiveRules activeRules = mock(ActiveRules.class);

  @Test
  public void should_be_disabled_by_default() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(activeRules, settings);
    assertThat(conf.isActive()).isFalse();
  }

  @Test
  public void should_be_enabled_if_at_least_one_rule_is_enabled() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(activeRules, settings);
    assertThat(conf.isActive()).isFalse();

    // clirr is still disabled
    settings.setProperty(ClirrConstants.REPORT_PATH, "foo.txt");
    assertThat(conf.isActive()).isFalse();

    org.sonar.api.batch.rule.ActiveRule activeRule = mock(org.sonar.api.batch.rule.ActiveRule.class);

    when(activeRules.find(ClirrConstants.RULE_NEW_API)).thenReturn(activeRule);
    assertThat(conf.isActive()).isTrue();

    when(activeRules.find(ClirrConstants.RULE_API_BEHAVIOR_CHANGE)).thenReturn(activeRule);
    assertThat(conf.isActive()).isTrue();

    when(activeRules.find(ClirrConstants.RULE_API_BREAK)).thenReturn(activeRule);
    assertThat(conf.isActive()).isTrue();
  }

  @Test
  public void test_rule_activation() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(activeRules, settings);
    assertThat(conf.isApiBehaviorChangeActive()).isFalse();
    assertThat(conf.isApiBreakActive()).isFalse();
    assertThat(conf.isNewApiActive()).isFalse();

    org.sonar.api.batch.rule.ActiveRule activeRule = mock(org.sonar.api.batch.rule.ActiveRule.class);

    when(activeRules.find(ClirrConstants.RULE_API_BREAK)).thenReturn(activeRule);
    assertThat(conf.isApiBreakActive()).isTrue();

    when(activeRules.find(ClirrConstants.RULE_API_BEHAVIOR_CHANGE)).thenReturn(activeRule);
    assertThat(conf.isApiBehaviorChangeActive()).isTrue();

    when(activeRules.find(ClirrConstants.RULE_NEW_API)).thenReturn(activeRule);
    assertThat(conf.isNewApiActive()).isTrue();
  }
}
