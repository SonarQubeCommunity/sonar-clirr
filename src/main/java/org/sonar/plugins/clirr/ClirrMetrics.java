package org.sonar.plugins.clirr;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class ClirrMetrics implements Metrics {

	public final static Metric TOTAL_API_CHANGES = new Metric("clirr-total-api-changes", "Total API Changes",
			"Number of API changes", Metric.ValueType.INT, Metric.DIRECTION_NONE, true, CoreMetrics.DOMAIN_RULES);
	public final static Metric API_BREAKS = new Metric("clirr-api-breaks", "API breaks",
			"Number of API changes which break the backward compatibility", Metric.ValueType.INT,
			Metric.DIRECTION_WORST, true, CoreMetrics.DOMAIN_RULES);
	public final static Metric API_BEHAVIOR_CHANGES = new Metric("clirr-api-behavior-changes", "API behavior changes",
			"Number of API changes which change the previous API behavior", Metric.ValueType.INT,
			Metric.DIRECTION_WORST, true, CoreMetrics.DOMAIN_RULES);
	public final static Metric NEW_API = new Metric("clirr-new-api", "New API", "Number of new API",
			Metric.ValueType.INT, Metric.DIRECTION_NONE, true, CoreMetrics.DOMAIN_RULES);

	public List<Metric> getMetrics() {
		return Arrays.asList(TOTAL_API_CHANGES, API_BREAKS, API_BEHAVIOR_CHANGES, NEW_API);
	}

}
