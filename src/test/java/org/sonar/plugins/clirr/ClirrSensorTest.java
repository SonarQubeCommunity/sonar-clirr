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
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.resources.Project;
import org.sonar.plugins.java.api.JavaResourceLocator;

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClirrSensorTest {

  private ClirrConfiguration configuration;
  private ClirrSensor sensor;
  private DefaultFileSystem fileSystem;

  @Before
  public void setUp() {
    configuration = mock(ClirrConfiguration.class);
    fileSystem = new DefaultFileSystem();
    sensor = new ClirrSensor(configuration, null, fileSystem, mock(JavaResourceLocator.class), mock(ResourcePerspectives.class));
  }

  @Test
  public void shouldExecuteOnProject() {
    Project project = mock(Project.class);

    when(configuration.isActive()).thenReturn(true);
    assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    when(configuration.isActive()).thenReturn(false);
    fileSystem.add(new DefaultInputFile("src/Foo.java").setLanguage("java").setType(Type.MAIN));
    assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    when(configuration.isActive()).thenReturn(true);
    assertThat(sensor.shouldExecuteOnProject(project)).isTrue();
  }

  @Test
  public void overridesToString() {
    assertThat(sensor.toString(), is("ClirrSensor"));
  }

}
