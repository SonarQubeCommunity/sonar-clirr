package org.sonar.plugins.clirr;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ClirrPluginTest {

	@Test
	public void testGetExtensions() {
		assertFalse(new ClirrPlugin().getExtensions().isEmpty());
	}

}
