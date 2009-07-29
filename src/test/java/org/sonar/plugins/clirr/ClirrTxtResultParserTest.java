package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

public class ClirrTxtResultParserTest {

	@Test
	public void parseResult() throws Exception {
		ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
		List<ClirrViolation> violations = clirrParser.parse(ClirrTxtResultParserTest.class
				.getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
		assertEquals(105, violations.size());
		assertEquals("ERROR", violations.get(0).getSeverity());
		assertEquals("8001", violations.get(0).getMessageId());
		assertEquals("org.sonar.plugins.api.AbstractLanguage", violations.get(0).getAffectedClass());
		assertEquals("Class org.sonar.plugins.api.AbstractLanguage removed", violations.get(0).getMessage());
	}
}
