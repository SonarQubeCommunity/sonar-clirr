package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;
import org.sonar.api.resources.JavaFile;

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
		assertEquals(new JavaFile("org.sonar.plugins.api.AbstractLanguage"), violations.get(0)
				.getJavaFile());
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
