package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.resources.JavaClass;

public class ClirrTxtResultParserTest {

	@Test
	public void parseResult() throws Exception {
		ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
		List<ClirrViolation> violations = clirrParser.parse(ClirrTxtResultParserTest.class
				.getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
		assertEquals(106, violations.size());
		assertEquals("ERROR", violations.get(0).getSeverity());
		assertEquals("8001", violations.get(0).getMessageId());
		assertEquals(new JavaClass("org.sonar.plugins.api.AbstractLanguage"), violations.get(0).getJavaClass());
		assertEquals("Class org.sonar.plugins.api.AbstractLanguage removed", violations.get(0).getMessage());
	}

	@Test
	public void parseToGetViolationsByResource() throws Exception {
		ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
		Map<JavaClass, List<ClirrViolation>> violations = clirrParser.parseToGetViolationsByResource(
				ClirrTxtResultParserTest.class.getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
		assertEquals(2, violations.get(new JavaClass("org.sonar.plugins.api.AbstractLanguage")).size());
		assertEquals(3, violations.get(new JavaClass("org.sonar.plugins.api.web.gwt.client.webservices.WSMetrics")).size());
	}
}
