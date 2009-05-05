package org.codehaus.sonar.clirrplugin;

import java.io.File;

import org.sonar.plugins.api.maven.AbstractMavenPluginHandler;
import org.sonar.plugins.api.maven.model.MavenPlugin;
import org.sonar.plugins.api.maven.model.MavenPom;

public class ClirrMavenPluginHandler extends AbstractMavenPluginHandler {

	@Override
	public void configurePlugin(MavenPom pom, MavenPlugin plugin) {

		//		plugin.setConfigParameter("encoding", System.getProperty("file.encoding"));
		//		plugin.setConfigParameter("linkXRef", "false");
		String xmlResultFile = new File(pom.getBuildDir(), ClirrPlugin.CLIRR_RESULT_XML).getAbsolutePath();
		String txtResultFile = new File(pom.getBuildDir(), ClirrPlugin.CLIRR_RESULT_TXT).getAbsolutePath();
		plugin.setConfigParameter("xmlOutputFile", xmlResultFile);
		plugin.setConfigParameter("textOutputFile", txtResultFile);
		plugin.setConfigParameter("minSeverity", "warning"); // info, warning*, error
	}

	public String getArtifactId() {
		return "clirr-maven-plugin";
	}

	public String[] getGoals() {
		return new String[] { "clirr" };
	}

	public String getGroupId() {
		return MavenPom.GROUP_ID_CODEHAUS_MOJO;
	}

	public boolean isFixedVersion() {
		return false;
	}

	public String getVersion() {
		return "2.2.2";
	}

}
