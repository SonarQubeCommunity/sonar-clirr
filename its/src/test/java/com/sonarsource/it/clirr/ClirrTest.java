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
import com.sonar.orchestrator.version.Version;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueQuery;
import org.sonar.wsclient.issue.Issues;

import static org.fest.assertions.Assertions.assertThat;

public class ClirrTest {

  private static Version artifactVersion;

  @ClassRule
  public static Orchestrator orchestrator = Orchestrator.builderEnv()
    .addPlugin(FileLocation.of("../target/sonar-clirr-plugin-" + artifactVersion() + ".jar"))
    .setOrchestratorProperty("javaVersion", "LATEST_RELEASE")
    .addPlugin("java")
    .build();

  @Before
  public void setUp() throws Exception {
    orchestrator.resetData();
    orchestrator.getServer().restoreProfile(FileLocation.ofClasspath("/clirr-profile.xml"));
  }

  private static Version artifactVersion() {
    if (artifactVersion == null) {
      try (FileInputStream fis = new FileInputStream(new File("../target/maven-archiver/pom.properties"))) {
        Properties props = new Properties();
        props.load(fis);
        artifactVersion = Version.create(props.getProperty("version"));
        return artifactVersion;
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
    return artifactVersion;
  }

  @Test
  public void should_report_clirr_issues_and_measures() throws IOException {

    // Install version 1.0 in local repository
    MavenBuild build = MavenBuild.create(new File("projects/sample-v1/pom.xml"))
      .setGoals("clean install");
    orchestrator.executeBuild(build);

    build = MavenBuild.create(new File("projects/sample-v2/pom.xml"))
      .setProperty("textOutputFile", "target/clirr-report.txt")
      .setProperty("failOnError", "false")
      .setProperty("comparisonVersion", "1.0")
      .setGoals("clean package clirr:check-no-fork");
    orchestrator.executeBuild(build);

    build = MavenBuild.create(new File("projects/sample-v2/pom.xml"))
      .setProfile("Clirr")
      .setProperty("sonar.clirr.reportPath", "target/clirr-report.txt")
      .setGoals("sonar:sonar");

    orchestrator.executeBuild(build);

    Issues issues = orchestrator.getServer().adminWsClient().issueClient().find(IssueQuery.create()
    // .componentRoots("com.sonarsource.it.clirr:simple-sample-clirr")
    );
    assertThat(issues.size()).isEqualTo(1);

    Issue issue = issues.list().get(0);
    assertThat(issue.ruleKey()).isEqualTo("clirr:clirr-api-break");
    assertThat(issue.componentKey()).isEqualTo("com.sonarsource.it.clirr:simple-sample-clirr:src/main/java/sample/MyApi.java");
  }

}
