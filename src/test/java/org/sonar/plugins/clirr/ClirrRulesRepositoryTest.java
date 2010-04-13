/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.sonar.api.rules.Rule;

public class ClirrRulesRepositoryTest {

	@Test
	public void getInitialReferential() {
		List<Rule> rules = new ClirrRulesRepository().getInitialReferential();
		assertEquals(3, rules.size());
	}

	@Test
	public void getRuleFromClirrViolation() {
		ClirrViolation error = new ClirrViolation("ERROR", "", "", "");
		assertEquals("clirr-api-break", new ClirrRulesRepository().getRuleFromClirrViolation(error).getKey());
		ClirrViolation warning = new ClirrViolation("WARNING", "", "", "");
		assertEquals("clirr-api-behavior-change", new ClirrRulesRepository().getRuleFromClirrViolation(warning).getKey());
		ClirrViolation info = new ClirrViolation("INFO", "", "", "");
		assertEquals("clirr-new-api", new ClirrRulesRepository().getRuleFromClirrViolation(info).getKey());
	}

}
