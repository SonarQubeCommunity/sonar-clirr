package org.sonar.plugins.clirr;

import java.util.ArrayList;
import java.util.List;

import org.sonar.commons.Metric;
import org.sonar.commons.Metric.ValueType;
import org.sonar.commons.rules.Rule;
import org.sonar.plugins.api.metrics.CoreMetrics;

/*
 * As the Metrics class implements org.sonar.plugins.api.Extension interface,
 * HelloWorldMetrics is a Sonar extension which allows to declare as many new Metrics as you want.
 */
public final class ClirrMetrics implements org.sonar.plugins.api.metrics.Metrics {

	private static final Integer DIRECTION = Integer.valueOf(-1);
	private static final boolean QUALITATIVE = true;
	private static final boolean USER_MANAGED = false;

	public static final Metric APIBREAKS = new Metric("apibreak", "API Breaks",
			"This metric represents errors in binary incompatibility to the previous version", Metric.ValueType.INT,
			DIRECTION, QUALITATIVE, CoreMetrics.DOMAIN_RULES, USER_MANAGED);

	// getMetrics() method is defined in the Metrics interface and is used by
	// Sonar to retrieve the list of new Metric
	public List<Metric> getMetrics() {
		List<Metric> metrics = new ArrayList<Metric>();
		createMetricPerRule(metrics);
		metrics.add(APIBREAKS);
		return metrics;
	}

	private void createMetricPerRule(List<Metric> metrics) {
		List<Rule> rules = new ClirrRulesRepository().getInitialReferential();
		for (Rule tag : rules) {
			String description = "Number of compatibility breaks of type '" + tag.getKey() + "' in the source code";
			metrics.add(new Metric(tag.getKey(), tag.getName(), description, ValueType.INT, DIRECTION, QUALITATIVE,
					CoreMetrics.DOMAIN_RULES, USER_MANAGED));
		}
	}
}