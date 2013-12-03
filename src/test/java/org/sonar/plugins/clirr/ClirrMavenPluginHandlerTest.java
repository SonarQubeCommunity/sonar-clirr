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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.maven.MavenUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

public class ClirrMavenPluginHandlerTest {

  private ClirrConfiguration configuration;
  private ClirrMavenPluginHandler handler;

  @Before
  public void setUp() {
    configuration = mock(ClirrConfiguration.class);
    handler = new ClirrMavenPluginHandler(configuration);
  }

  @Test
  public void defineMavenPlugin() {
    assertThat(handler.getGroupId(), is(MavenUtils.GROUP_ID_CODEHAUS_MOJO));
    assertThat(handler.getArtifactId(), is("clirr-maven-plugin"));
    assertThat(handler.getGoals(), equalTo(new String[] { "clirr" }));
    assertThat(handler.getVersion(), notNullValue());
    assertThat(handler.isFixedVersion(), is(false));
  }
}
