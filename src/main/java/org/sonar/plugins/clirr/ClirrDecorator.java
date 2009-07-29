package org.sonar.plugins.clirr;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.batch.AbstractSumChildrenDecorator;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Project;

public class ClirrDecorator extends AbstractSumChildrenDecorator {

	@DependedUpon
	public List<Metric> generatesMetrics() {
		return Arrays.asList(ClirrMetrics.TOTAL_API_CHANGES, ClirrMetrics.API_BREAKS,
				ClirrMetrics.API_BEHAVIOR_CHANGES, ClirrMetrics.NEW_API);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean shouldExecuteOnProject(Project project) {
		return project.getLanguage().equals(Java.KEY);
	}

	protected boolean shouldSaveZeroIfNoChildMeasures() {
		return false;
	}

}
