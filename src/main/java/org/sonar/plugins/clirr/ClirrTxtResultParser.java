/*
 * SonarQube Clirr Plugin
 * Copyright (C) 2009 SonarSource
 * sonarqube@googlegroups.com
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
package org.sonar.plugins.clirr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public final class ClirrTxtResultParser {

  private static final int FIELDCOUNT = 4;
  private static final int IDX_SEVERITY = 0;
  private static final int IDX_ERRCODE = 1;
  private static final int IDX_CLASS = 2;
  private static final int IDX_MESSAGE = 3;

  public List<ClirrViolation> parse(final InputStream input, final Charset charset) throws IOException {
    return parse(new InputStreamReader(input, charset));
  }

  public List<ClirrViolation> parse(final Reader input) throws IOException {
    List<ClirrViolation> violations = new ArrayList<>();
    List<String> lines = IOUtils.readLines(input);
    for (String line : lines) {
      String[] split = line.split(Pattern.quote(":"));
      if (split.length >= FIELDCOUNT) {
        violations.add(parseViolationLine(split));
      }
    }
    return violations;
  }

  private static ClirrViolation parseViolationLine(String[] split) {
    return new ClirrViolation(split[IDX_SEVERITY].trim(), split[IDX_ERRCODE].trim(),
      split[IDX_MESSAGE].trim(), split[IDX_CLASS].trim());
  }
}
