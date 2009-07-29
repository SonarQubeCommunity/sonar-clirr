package org.sonar.plugins.clirr;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.batch.maven.MavenUtils;
import org.sonar.api.resources.Project;

public final class ClirrMavenPluginHandler implements MavenPluginHandler {

	public String getArtifactId() {
		return "clirr-maven-plugin";
	}

	public String[] getGoals() {
		return new String[] { "clirrs" };
	}

	public String getGroupId() {
		return MavenUtils.GROUP_ID_CODEHAUS_MOJO;
	}

	public boolean isFixedVersion() {
		return false;
	}

	public String getVersion() {
		return "2.2.2";
	}

	public void configure(Project project, MavenPlugin plugin) {
		String comparisonVersion = project.getConfiguration().getString(ClirrPlugin.CLIRR_KEY_COMPARISON_VERSION);
		if (!StringUtils.isBlank(comparisonVersion)) {
			plugin.setConfigParameter("comparisonVersion", comparisonVersion);
		}

		plugin.setConfigParameter("textOutputFile", ClirrPlugin.CLIRR_RESULT_TXT);

		String[] wildcardPatterns = project.getExclusionPatterns();
		for (String excludePattern : wildcardPatterns) {
			plugin.getConfiguration().addParameter("excludes/exclude", excludePattern);
		}
	}

	public boolean dependsUponCustomRules() {
		return false;
	}
}
