/*
 * SonarQube Clirr Plugin
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
package org.sonar.plugins.clirr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClirrViolationTest {

  @Test
  public void getAffectedClass() {
    ClirrViolation violation = new ClirrViolation("", "", "", this.getClass().getCanonicalName());
    assertEquals("org.sonar.plugins.clirr.ClirrViolationTest", violation.getAffectedClass());
  }

  @Test
  public void shouldGetRuleKey() {
    ClirrViolation error = new ClirrViolation("ERROR", "", "", "");
    assertEquals("clirr-api-break", error.getRuleKey().rule());

    ClirrViolation warning = new ClirrViolation("WARNING", "", "", "");
    assertEquals("clirr-api-behavior-change", warning.getRuleKey().rule());

    ClirrViolation info = new ClirrViolation("INFO", "", "", "");
    assertEquals("clirr-new-api", info.getRuleKey().rule());
  }
}
