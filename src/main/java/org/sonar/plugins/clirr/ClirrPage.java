/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
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

import org.apache.commons.io.FileUtils;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.api.web.views.*;

import java.io.File;
import java.io.IOException;

@NavigationSection(NavigationSection.PROJECT)
@UserRole(UserRole.VIEWER)
@ResourceLanguage(Java.KEY)
@ResourceQualifier({Project.QUALIFIER_PROJECT, Project.QUALIFIER_MODULE, Project.QUALIFIER_PACKAGE})
public class ClirrPage implements RubyRailsPage {

  public String getTemplate() {
    try {
      //FIXME
      return FileUtils.readFileToString(new File("/Users/simon/Desktop/clirr.erb"), "UTF-8");
    } catch (IOException e) {
      throw new SonarException("Can not load the file");
    }
  }

  public String getId() {
    return "clirr-page";
  }

  public String getTitle() {
    return "Clirr";
  }
}
