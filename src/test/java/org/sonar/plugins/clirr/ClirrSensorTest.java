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

package org.sonar.plugins.clirr;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClirrSensorTest {

  private ClirrConfiguration configuration;
  private ClirrSensor sensor;

  @Before
  public void setUp() {
    configuration = mock(ClirrConfiguration.class);
    sensor = new ClirrSensor(configuration, null, null);
  }

  @Test
  public void shouldExecuteOnProject() {
    Project project = mock(Project.class);
    Language anotherLanguage = mock(Language.class);
    when(project.getLanguage()).thenReturn(Java.INSTANCE, Java.INSTANCE, anotherLanguage);
    when(configuration.isActive()).thenReturn(true, false, true);

    assertThat(sensor.shouldExecuteOnProject(project), is(true));
    assertThat(sensor.shouldExecuteOnProject(project), is(false));
    assertThat(sensor.shouldExecuteOnProject(project), is(false));
  }

  @Test
  public void overridesToString() {
    assertThat(sensor.toString(), is("ClirrSensor"));
  }

}
