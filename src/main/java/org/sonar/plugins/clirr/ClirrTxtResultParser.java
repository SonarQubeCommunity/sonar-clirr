package org.sonar.plugins.clirr;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ClirrTxtResultParser {

  public List<ClirrViolation> parse(final InputStream input, final Charset charset) throws IOException {
    List<ClirrViolation> violations = new ArrayList<ClirrViolation>();
    List<String> lines = IOUtils.readLines(input, charset.name());
    for (String line : lines) {
      String[] split = line.split(Pattern.quote(":"));
      if (split.length >= 4) {
        violations.add(parseViolationLine(split));
      }
    }
    return violations;
  }

  private ClirrViolation parseViolationLine(String[] split) {
    return new ClirrViolation(split[0].trim(), split[1].trim(), split[3].trim(), split[2].trim());
  }
}
