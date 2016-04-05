# SonarQube Clirr Plugin

## Description / Features
[Clirr](http://clirr.sourceforge.net/) checks Java libraries for binary and source compatibility with older releases. In a continuous integration process Clirr can automatically prevent accidental introduction of binary or source compatibility problems.

It can measure the number of API break issues between the current sources and another version of the library. The reference version can be automatically downloaded from the Maven repository when using the Clirr Maven plugin.

## Configuration
1. Add at least one Clirr rule to your quality profile (Quality Profiles > Select your profile > filter rules on repository "Clirr"):
  - API Change adds new feature without breaking anything
  - API Change breaks the backward binary compatibility
  - API Change might change runtime expected behavior
1. Configure path of Clirr text report using property sonar.clirr.reportPath
1. Analyze your project
1. Check for new issues from Clirr rule repository.

## Produce Clirr text report using Maven

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>clirr-maven-plugin</artifactId>
      <configuration>
        <comparisonVersion>X.Y</comparisonVersion>
        <textOutputFile>${project.build.directory}/clirr-report.txt</textOutputFile>
        <linkXRef>false</linkXRef>
        <failOnError>false</failOnError>
      </configuration>
      <executions>
        <execution>
          <id>clirr</id>
          <goals>
            <goal>check-no-fork</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```
