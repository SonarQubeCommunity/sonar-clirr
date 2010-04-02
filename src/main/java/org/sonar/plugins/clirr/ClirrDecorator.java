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

import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.measures.MeasureUtils;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Violation;

import java.util.Arrays;
import java.util.List;

public class ClirrDecorator implements Decorator {

  private final ClirrConfiguration configuration;

  public ClirrDecorator(ClirrConfiguration configuration) {
    this.configuration = configuration;
  }

  @DependedUpon
  public List<Metric> generatesMetrics() {
    return Arrays.asList(
        ClirrMetrics.TOTAL_API_CHANGES,
        ClirrMetrics.API_BREAKS,
        ClirrMetrics.API_BEHAVIOR_CHANGES,
        ClirrMetrics.NEW_API);
  }

  /**
   * {@inheritDoc}
   */
  public boolean shouldExecuteOnProject(Project project) {
    return project.getLanguage().equals(Java.INSTANCE);
  }

  public void decorate(Resource resource, DecoratorContext context) {
    int apiBreaks = 0;
    boolean active = false;
    if (configuration.isApiBreakActive()) {
      apiBreaks = saveValue(context, ClirrMetrics.API_BREAKS, ClirrConstants.RULE_API_BREAK);
      active = true;
    }

    int apiBehaviorChanges = 0;
    if (configuration.isApiBehaviorChangeActive()) {
      apiBehaviorChanges = saveValue(context, ClirrMetrics.API_BEHAVIOR_CHANGES, ClirrConstants.RULE_API_BEHAVIOR_CHANGE);
      active = true;
    }

    int newApi = 0;
    if (configuration.isNewApiActive()) {
      newApi = saveValue(context, ClirrMetrics.NEW_API, ClirrConstants.RULE_NEW_API);
      active = true;
    }

    if (active) {
      context.saveMeasure(ClirrMetrics.TOTAL_API_CHANGES, (double) (apiBreaks + apiBehaviorChanges + newApi));
    }
  }

  private int saveValue(DecoratorContext context, Metric metric, String ruleKey) {
    int result;
    result = MeasureUtils.sum(true, context.getChildrenMeasures(metric)).intValue();
    for (Violation violation : context.getViolations()) {
      if (violation.getRule().getKey().equals(ruleKey)) {
        result++;
      }
    }
    context.saveMeasure(metric, (double) result);
    return result;
  }
}
