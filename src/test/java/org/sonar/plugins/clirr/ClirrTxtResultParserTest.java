package org.sonar.plugins.clirr;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.sonar.api.resources.JavaFile;

import java.nio.charset.Charset;
import java.util.List;

public class ClirrTxtResultParserTest {

  @Test
  public void parseResult() throws Exception {
    ClirrTxtResultParser clirrParser = new ClirrTxtResultParser();
    List<ClirrViolation> violations = clirrParser.parse(ClirrTxtResultParserTest.class
        .getResourceAsStream("/clirr-report.txt"), Charset.defaultCharset());
    assertEquals(106, violations.size());
    assertEquals("ERROR", violations.get(0).getSeverity());
    assertEquals("8001", violations.get(0).getMessageId());
    assertEquals(new JavaFile("org.sonar.plugins.api.AbstractLanguage"), violations.get(0).getJavaFile());
    assertEquals("Class org.sonar.plugins.api.AbstractLanguage removed", violations.get(0).getMessage());
  }

}
