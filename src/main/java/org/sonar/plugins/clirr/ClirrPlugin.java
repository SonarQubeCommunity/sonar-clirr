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
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

@Properties({
  @Property(
    key = ClirrConstants.REPORT_PATH,
    name = "Report path",
    description = "Path of the Clirr text report file. Can be absolute or relative to module baseDir.",
    project = true,
    module = true,
    global = false)
})
public final class ClirrPlugin extends SonarPlugin {

  @Override
  public List getExtensions() {
    return Arrays.asList(ClirrSensor.class, ClirrRulesDefinition.class,
      ClirrDecorator.class, ClirrMetrics.class, ClirrWidget.class, ClirrConfiguration.class);
  }
}
