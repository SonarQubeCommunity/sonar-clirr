package org.sonar.plugins.clirr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClirrMetricsTest {

	@Test
	public void testGetMetricsNotEmpty() {
		assertFalse(new ClirrMetrics().getMetrics().isEmpty());
	}

	@Test
	public void testGetMetricsContainsAggregatedApiBreaks() {
		assertTrue(new ClirrMetrics().getMetrics().contains(ClirrMetrics.APIBREAKS));
	}

}
