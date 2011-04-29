/*
 * Sonar Clirr Plugin
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

/*
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

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class ClirrMetrics implements Metrics {

  public static final String API_COMPATIBILITY_DOMAIN = "API compatibility";

  public static final Metric TOTAL_API_CHANGES = new Metric.Builder(
      "clirr_total_api_changes", "Total API Changes", Metric.ValueType.INT)
      .setDescription("Number of API changes")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(API_COMPATIBILITY_DOMAIN)
      .setOptimizedBestValue(true)
      .setBestValue(0.0)
      .create();

  public static final Metric API_BREAKS = new Metric.Builder("clirr_api_breaks", "API breaks", Metric.ValueType.INT)
      .setDescription("Number of API changes which break the backward compatibility")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(true)
      .setDomain(API_COMPATIBILITY_DOMAIN)
      .setOptimizedBestValue(true)
      .setBestValue(0.0)
      .create();

  public static final Metric API_BEHAVIOR_CHANGES = new Metric.Builder(
      "clirr_api_behavior_changes", "API behavior changes", Metric.ValueType.INT)
      .setDescription("Number of API changes which change the previous API behavior")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(true)
      .setDomain(API_COMPATIBILITY_DOMAIN)
      .setOptimizedBestValue(true)
      .setBestValue(0.0)
      .create();

  public static final Metric NEW_API = new Metric.Builder(
      "clirr_new_api", "New API", Metric.ValueType.INT)
      .setDescription("Number of new API")
      .setDirection(Metric.DIRECTION_WORST)
      .setQualitative(false)
      .setDomain(API_COMPATIBILITY_DOMAIN)
      .setOptimizedBestValue(true)
      .setBestValue(0.0)
      .create();

  public List<Metric> getMetrics() {
    return Arrays.asList(TOTAL_API_CHANGES, API_BREAKS, API_BEHAVIOR_CHANGES, NEW_API);
  }

}
