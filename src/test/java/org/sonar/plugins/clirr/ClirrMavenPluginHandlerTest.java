package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.configuration.Configuration;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Test;
import org.sonar.plugins.api.maven.Exclusions;
import org.sonar.plugins.api.maven.model.MavenPlugin;
import org.sonar.plugins.api.maven.model.MavenPom;

public class ClirrMavenPluginHandlerTest {

	@Test
	public void testNoExclusions() {
		Configuration configuration = mock(Configuration.class);
		Exclusions exclusions = new Exclusions(configuration);

		MavenPlugin plugin = executePluginHandlerConfiguration(exclusions);

		// Test for mandatory configuration for Sonar Clirr Plugin
		assertTrue(plugin.getConfigParameter(PluginParameters.TEXTOUTPUTFILE).length() > 0);

		// No excludes, no <excludes> configuration
		assertNull(plugin.getConfiguration().getXpp3Dom().getChild(PluginParameters.EXCLUDES));
	}

	@Test
	public void testWithExclusions() {
		final String FIRST_EXCLUDE_PATTERN = "foo";
		final String SECOND_EXCLUDE_PATTERN = "bar";
		Exclusions exclusions = new Exclusions(null) {
			@Override
			public String[] getWildcardPatterns() {
				return new String[] { FIRST_EXCLUDE_PATTERN, SECOND_EXCLUDE_PATTERN };
			}
		};

		MavenPlugin plugin = executePluginHandlerConfiguration(exclusions);

		Xpp3Dom child = plugin.getConfiguration().getXpp3Dom().getChild(PluginParameters.EXCLUDES);
		Xpp3Dom[] children = child.getChildren(PluginParameters.EXCLUDE);
		assertTrue(children.length == 2);
		assertEquals(FIRST_EXCLUDE_PATTERN, children[0].getValue());
		assertEquals(SECOND_EXCLUDE_PATTERN, children[1].getValue());
	}

	private MavenPlugin executePluginHandlerConfiguration(Exclusions exclusions) {
		ClirrMavenPluginHandler handler = new ClirrMavenPluginHandler(exclusions);
		MavenProject mavenProject = new MavenProject();
		mavenProject.getBuild().setDirectory(".");
		MavenPom pom = new MavenPom(mavenProject);
		MavenPlugin plugin = new MavenPlugin(null, null);
		handler.configurePlugin(pom, plugin);
		return plugin;
	}

}
