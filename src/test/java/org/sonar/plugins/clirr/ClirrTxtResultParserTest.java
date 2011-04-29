/*
 * Sonar Clirr Plugin
 * Copyright (C) 2009 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

/*
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.clirr;

import org.junit.Test;
import org.sonar.java.api.JavaClass;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClirrTxtResultParserTest {

	private static class RepeatingStringReader extends StringReader {

		private int repeats;

		public RepeatingStringReader(String s, int repeats) {
			super(s);
			this.repeats = repeats;
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			int read = super.read(cbuf, off, len);
			if (read == -1 && --repeats > 0) {
				reset();
				return super.read(cbuf, off, len);
			}
			return read;
		}

	}

	@Test
	public void parseResult() throws Exception {
		ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
		List<ClirrViolation> violations = clirrParser.parse(ClirrTxtResultParserTest.class
				.getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
		assertEquals(106, violations.size());
		assertEquals("ERROR", violations.get(0).getSeverity());
		assertEquals("8001", violations.get(0).getMessageId());
		assertEquals(JavaClass.create("org.sonar.plugins.api.AbstractLanguage"), violations.get(0).getJavaClass());
		assertEquals("Class org.sonar.plugins.api.AbstractLanguage removed", violations.get(0)
				.getMessage());
	}

	@Test
	public void largeReport() throws Exception {
		final int repeats = 10000;
		final String repeatedLine = "ERROR: 8001: org.sonar.plugins.api.AbstractLanguage: Class org.sonar.plugins.api.AbstractLanguage removed\n";
		ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
		StringReader reader = new RepeatingStringReader(repeatedLine, repeats);
		List<ClirrViolation> violations = clirrParser.parse(reader);
		assertEquals(repeats, violations.size());
	}

}
