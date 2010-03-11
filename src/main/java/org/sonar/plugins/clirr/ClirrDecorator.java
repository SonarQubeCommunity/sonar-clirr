package org.sonar.plugins.clirr;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.measures.MeasureUtils;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Violation;

public class ClirrDecorator implements Decorator {

	private final ClirrRulesRepository rulesRepo;

	public ClirrDecorator(ClirrRulesRepository rulesRepo) {
		this.rulesRepo = rulesRepo;
	}

	@DependedUpon
	public List<Metric> generatesMetrics() {
		return Arrays.asList(ClirrMetrics.TOTAL_API_CHANGES, ClirrMetrics.API_BREAKS,
				ClirrMetrics.API_BEHAVIOR_CHANGES, ClirrMetrics.NEW_API);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean shouldExecuteOnProject(Project project) {
		return project.getLanguage().equals(Java.INSTANCE);
	}

	public void decorate(Resource resource, DecoratorContext context) {
		int apiBreaks = MeasureUtils
				.sum(true, context.getChildrenMeasures(ClirrMetrics.API_BREAKS)).intValue();
		int apiBehaviorChanges = MeasureUtils.sum(true,
				context.getChildrenMeasures(ClirrMetrics.API_BEHAVIOR_CHANGES)).intValue();
		int newApi = MeasureUtils.sum(true, context.getChildrenMeasures(ClirrMetrics.NEW_API))
				.intValue();

		for (Violation violation : context.getViolations()) {
			if (violation.getRule().equals(rulesRepo.getApiBreakRule())) {
				apiBreaks++;
			} else if (violation.getRule().equals(rulesRepo.getApiBehaviorChangeRule())) {
				apiBehaviorChanges++;
			} else if (violation.getRule().equals(rulesRepo.getNewApiRule())) {
				newApi++;
			}
		}

		context.saveMeasure(ClirrMetrics.API_BREAKS, (double) apiBreaks);
		context.saveMeasure(ClirrMetrics.API_BEHAVIOR_CHANGES, (double) apiBehaviorChanges);
		context.saveMeasure(ClirrMetrics.NEW_API, (double) newApi);
		context.saveMeasure(ClirrMetrics.TOTAL_API_CHANGES, (double) (apiBreaks
				+ apiBehaviorChanges + newApi));
	}
}
