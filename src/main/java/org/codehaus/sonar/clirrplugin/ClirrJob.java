package org.codehaus.sonar.clirrplugin;

import java.util.List;

import org.sonar.commons.Language;
import org.sonar.commons.Languages;
import org.sonar.commons.Metric;
import org.sonar.commons.resources.Measure;
import org.sonar.commons.resources.Resource;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.jobs.AbstractJob;
import org.sonar.plugins.api.jobs.JobContext;

public final class ClirrJob extends AbstractJob {

	public ClirrJob(Languages languages) {
		super(languages);
	}

	@Override
	protected boolean shouldExecuteOnLanguage(Language language) {
		return language.equals(new Java());
	}

	public boolean shouldExecuteOnResource(Resource resource) {
		return !resource.isFile();
	}

	public void execute(JobContext jobContext) {
		List<Metric> tags = new ClirrMetrics().getMetrics();
		for (Metric tag : tags) {
			List<Measure> childrenMeasures = jobContext.getChildrenMeasures(tag);
			if (childrenMeasures != null && childrenMeasures.size() > 0) {
				Double sum = 0.0;
				boolean hasChildrenMeasures = false;
				for (Measure measure : childrenMeasures) {
					if (measure.getValue() != null) {
						sum += measure.getValue();
						hasChildrenMeasures = true;
					}
				}
				if (hasChildrenMeasures) {
					jobContext.addMeasure(tag, sum);
				}
			}
		}
	}

}
