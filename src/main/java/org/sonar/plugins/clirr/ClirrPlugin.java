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

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

@Properties({
  @Property(
    key = ClirrConstants.COMPARISON_VERSION_PROPERTY,
    name = "Reference version",
    description = "By default, Clirr compares the current code against the latest released version. Use this parameter to compare" +
      " your code against a particular version.",
    project = true,
    module = true,
    global = false
  ),
  @Property(
    key = ClirrConstants.API_PROPERTY,
    type = PropertyType.BOOLEAN,
    defaultValue = "false",
    name = "API",
    description = "Only API projects are checked. Set to true if this project is considered as an API.",
    project = true,
    module = true,
    global = false
  )
})
public final class ClirrPlugin extends SonarPlugin {

  public List getExtensions() {
    return Arrays.asList(ClirrSensor.class, ClirrRulesRepository.class, ClirrMavenPluginHandler.class,
      ClirrDecorator.class, ClirrMetrics.class, ClirrWidget.class, ClirrConfiguration.class);
  }
}
