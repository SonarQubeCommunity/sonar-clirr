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

import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClirrConfigurationTest {

  Settings settings = new Settings();
  RulesProfile profile = mock(RulesProfile.class);

  @Test
  public void should_be_disabled_by_default() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(profile, settings);
    assertThat(conf.isActive()).isFalse();
  }

  @Test
  public void should_be_enabled_if_at_least_one_rule_is_enabled() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(profile, settings);
    assertThat(conf.isActive()).isFalse();

    // clirr is still disabled
    settings.setProperty(ClirrConstants.API_PROPERTY, true);
    assertThat(conf.isActive()).isFalse();

    when(profile.getActiveRule(ClirrConstants.PLUGIN_KEY, ClirrConstants.RULE_NEW_API)).thenReturn(new ActiveRule());
    assertThat(conf.isActive()).isTrue();

    when(profile.getActiveRule(ClirrConstants.PLUGIN_KEY, ClirrConstants.RULE_API_BEHAVIOR_CHANGE)).thenReturn(new ActiveRule());
    assertThat(conf.isActive()).isTrue();

    when(profile.getActiveRule(ClirrConstants.PLUGIN_KEY, ClirrConstants.RULE_API_BREAK)).thenReturn(new ActiveRule());
    assertThat(conf.isActive()).isTrue();
  }

  @Test
  public void should_get_comparison_version() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(profile, settings);
    assertThat(conf.getComparisonVersion()).isNull();
    assertThat(conf.hasComparisonVersion()).isFalse();

    settings.setProperty(ClirrConstants.COMPARISON_VERSION_PROPERTY, "2.3");
    assertThat(conf.getComparisonVersion()).isEqualTo("2.3");
    assertThat(conf.hasComparisonVersion()).isTrue();
  }

  @Test
  public void test_rule_activation() throws Exception {
    ClirrConfiguration conf = new ClirrConfiguration(profile, settings);
    assertThat(conf.isApiBehaviorChangeActive()).isFalse();
    assertThat(conf.isApiBreakActive()).isFalse();
    assertThat(conf.isNewApiActive()).isFalse();


    when(profile.getActiveRule(ClirrConstants.PLUGIN_KEY, ClirrConstants.RULE_API_BREAK)).thenReturn(new ActiveRule());
    assertThat(conf.isApiBreakActive()).isTrue();

    when(profile.getActiveRule(ClirrConstants.PLUGIN_KEY, ClirrConstants.RULE_API_BEHAVIOR_CHANGE)).thenReturn(new ActiveRule());
    assertThat(conf.isApiBehaviorChangeActive()).isTrue();

    when(profile.getActiveRule(ClirrConstants.PLUGIN_KEY, ClirrConstants.RULE_NEW_API)).thenReturn(new ActiveRule());
    assertThat(conf.isNewApiActive()).isTrue();
  }
}
