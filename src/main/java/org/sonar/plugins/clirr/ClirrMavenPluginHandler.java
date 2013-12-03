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

import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.batch.maven.MavenUtils;
import org.sonar.api.resources.Project;

public final class ClirrMavenPluginHandler implements MavenPluginHandler {

  private ClirrConfiguration configuration;

  public ClirrMavenPluginHandler(ClirrConfiguration configuration) {
    this.configuration = configuration;
  }

  public String getArtifactId() {
    return "clirr-maven-plugin";
  }

  public String[] getGoals() {
    return new String[]{"clirr"};
  }

  public String getGroupId() {
    return MavenUtils.GROUP_ID_CODEHAUS_MOJO;
  }

  public boolean isFixedVersion() {
    return false;
  }

  public String getVersion() {
    return "2.2.2";
  }

  public void configure(Project project, MavenPlugin plugin) {
    if (configuration.hasComparisonVersion()) {
      plugin.setParameter("comparisonVersion", configuration.getComparisonVersion());
    }

    plugin.setParameter("textOutputFile", project.getFileSystem().getSonarWorkingDirectory() + "/" + ClirrConstants.RESULT_TXT);

    String[] wildcardPatterns = project.getExclusionPatterns();
    for (String excludePattern : wildcardPatterns) {
      plugin.addParameter("excludes/exclude", excludePattern);
    }
  }
}
