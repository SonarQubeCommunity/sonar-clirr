/*
 * Clirr Plugin :: Integration Tests
 * Copyright (C) 2014 ${owner}
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
package com.sonarsource.it.clirr;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.MavenBuild;
import com.sonar.orchestrator.locator.FileLocation;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueQuery;
import org.sonar.wsclient.issue.Issues;
import org.sonar.wsclient.services.ResourceQuery;

import static org.fest.assertions.Assertions.assertThat;

public class ClirrTest {

  @ClassRule
  public static Orchestrator orchestrator = Orchestrator.builderEnv()
    .addPlugin(FileLocation.of("../target/sonar-clirr-plugin.jar"))
    .setOrchestratorProperty("javaVersion", "LATEST_RELEASE")
    .addPlugin("java")
    .build();

  @Before
  public void setUp() throws Exception {
    orchestrator.resetData();

    orchestrator.getServer().restoreProfile(FileLocation.ofClasspath("/clirr-profile.xml"));
  }

  @Test
  public void should_report_clirr_issues_and_measures() throws IOException {

    // Install version 1.0 in local repository
    MavenBuild build = MavenBuild.create(new File("projects/sample-v1/pom.xml"))
      .setGoals("clean install");
    orchestrator.executeBuild(build);

    build = MavenBuild.create(new File("projects/sample-v2/pom.xml"))
      .setProperty("textOutputFile", "target/clirr-report.txt")
      .setProperty("comparisonVersion", "1.0")
      .setGoals("clean package clirr:clirr");
    orchestrator.executeBuild(build);

    build = MavenBuild.create(new File("projects/sample-v2/pom.xml"))
      .setProfile("Clirr")
      .setProperty("sonar.clirr.reportPath", "target/clirr-report.txt")
      .setGoals("sonar:sonar");

    orchestrator.executeBuild(build);

    Issues issues = orchestrator.getServer().adminWsClient().issueClient().find(IssueQuery.create()
    // .componentRoots("com.sonarsource.it.samples:simple-sample-clirr")
    );
    assertThat(issues.size()).isEqualTo(1);

    Issue issue = issues.list().get(0);
    assertThat(issue.ruleKey()).isEqualTo("clirr:clirr-api-break");
    assertThat(issue.componentKey()).isEqualTo("com.sonarsource.it.samples:simple-sample-clirr:src/main/java/sample/MyApi.java");

    assertThat(orchestrator.getServer().getAdminWsClient().find(new ResourceQuery("com.sonarsource.it.samples:simple-sample-clirr")
      .setMetrics("clirr_api_breaks")).getMeasureIntValue("clirr_api_breaks")).isEqualTo(1);
  }

}
