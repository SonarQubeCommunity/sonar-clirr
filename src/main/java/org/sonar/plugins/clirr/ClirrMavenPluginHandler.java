package org.sonar.plugins.clirr;

import java.io.File;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonar.plugins.api.maven.AbstractMavenPluginHandler;
import org.sonar.plugins.api.maven.Exclusions;
import org.sonar.plugins.api.maven.model.MavenPlugin;
import org.sonar.plugins.api.maven.model.MavenPom;

public final class ClirrMavenPluginHandler extends AbstractMavenPluginHandler {

	private final Exclusions exclusions;

	public ClirrMavenPluginHandler(Exclusions exclusions) {
		this.exclusions = exclusions;
	}

	/**
	 * Let the clirr-maven-plugin produce its results as xml and txt. We'll
	 * parse only the text file by default, which is faster and contains more
	 * information.
	 */
	@Override
	public void configurePlugin(MavenPom pom, MavenPlugin plugin) {
		String xmlResultFile = new File(pom.getBuildDir(), ClirrPlugin.CLIRR_RESULT_XML).getAbsolutePath();
		String txtResultFile = new File(pom.getBuildDir(), ClirrPlugin.CLIRR_RESULT_TXT).getAbsolutePath();
		plugin.setConfigParameter(PluginParameters.XMLOUTPUTFILE, xmlResultFile);
		plugin.setConfigParameter(PluginParameters.TEXTOUTPUTFILE, txtResultFile);
		plugin.setConfigParameter(PluginParameters.MINSEVERITY, PluginParameters.SEVERITY_WARNING);

		String[] wildcardPatterns = exclusions.getWildcardPatterns();
		if (wildcardPatterns != null && wildcardPatterns.length > 0) {
			Xpp3Dom excludesDom = new Xpp3Dom(PluginParameters.EXCLUDES);
			for (String excludePattern : wildcardPatterns) {
				Xpp3Dom excludeDom = new Xpp3Dom(PluginParameters.EXCLUDE);
				excludeDom.setValue(excludePattern);
				excludesDom.addChild(excludeDom);
			}
			plugin.getConfiguration().getXpp3Dom().addChild(excludesDom);
		}
	}

	public String getArtifactId() {
		return "clirr-maven-plugin";
	}

	public String[] getGoals() {
		return new String[] { PluginParameters.GOAL };
	}

	public String getGroupId() {
		return MavenPom.GROUP_ID_CODEHAUS_MOJO;
	}

	/**
	 * If this method returns <tt>false</tt>, then newest version of
	 * clirr-maven-plugin is resolved. if <tt>true</tt> is returned, the version
	 * of clirr-maven-plugin is used as defined by {@link #getVersion()}.
	 */
	public boolean isFixedVersion() {
		return false;
	}

	/**
	 * @see #isFixedVersion()
	 */
	public String getVersion() {
		return "2.2.2";
	}

}
