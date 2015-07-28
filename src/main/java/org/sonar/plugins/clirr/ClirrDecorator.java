/*
 * SonarQube Clirr Plugin
 * Copyright (C) 2009 SonarSource
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
package org.sonar.plugins.clirr;

import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.measures.MeasureUtils;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;

import java.util.Arrays;
import java.util.List;

public class ClirrDecorator implements Decorator {

  private final ClirrConfiguration configuration;
  private final ResourcePerspectives perspectives;

  public ClirrDecorator(ClirrConfiguration configuration, ResourcePerspectives perspectives) {
    this.configuration = configuration;
    this.perspectives = perspectives;
  }

  @DependedUpon
  public List<Metric> generatesMetrics() {
    return Arrays.<Metric>asList(
      ClirrMetrics.TOTAL_API_CHANGES,
      ClirrMetrics.API_BREAKS,
      ClirrMetrics.API_BEHAVIOR_CHANGES,
      ClirrMetrics.NEW_API);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return true;
  }

  @Override
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

  private int saveValue(DecoratorContext context, Metric metric, RuleKey ruleKey) {
    int result;
    result = MeasureUtils.sum(true, context.getChildrenMeasures(metric)).intValue();
    Issuable issuable = perspectives.as(Issuable.class, context.getResource());
    if (issuable != null) {
      for (Issue issue : issuable.issues()) {
        if (issue.ruleKey().equals(ruleKey)) {
          result++;
        }
      }
    }

    context.saveMeasure(metric, (double) result);
    return result;
  }
}
