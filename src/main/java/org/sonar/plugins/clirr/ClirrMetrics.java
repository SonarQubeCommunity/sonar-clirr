package org.sonar.plugins.clirr;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class ClirrMetrics implements Metrics {

  public static final String API_COMPATIBILITY_DOMAIN = "API compatibility";

  public static final Metric TOTAL_API_CHANGES = new Metric("clirr_total_api_changes", "Total API Changes",
      "Number of API changes", Metric.ValueType.INT, Metric.DIRECTION_WORST, false, API_COMPATIBILITY_DOMAIN);

  public static final Metric API_BREAKS = new Metric("clirr_api_breaks", "API breaks",
      "Number of API changes which break the backward compatibility", Metric.ValueType.INT,
      Metric.DIRECTION_WORST, true, API_COMPATIBILITY_DOMAIN);

  public static final Metric API_BEHAVIOR_CHANGES = new Metric("clirr_api_behavior_changes", "API behavior changes",
      "Number of API changes which change the previous API behavior", Metric.ValueType.INT,
      Metric.DIRECTION_WORST, true, API_COMPATIBILITY_DOMAIN);

  public static final Metric NEW_API = new Metric("clirr_new_api", "New API", "Number of new API",
      Metric.ValueType.INT, Metric.DIRECTION_WORST, false, API_COMPATIBILITY_DOMAIN);

  public List<Metric> getMetrics() {
    return Arrays.asList(TOTAL_API_CHANGES, API_BREAKS, API_BEHAVIOR_CHANGES, NEW_API);
  }

}
