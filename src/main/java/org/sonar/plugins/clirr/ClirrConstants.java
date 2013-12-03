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

public interface ClirrConstants {

  String PLUGIN_KEY = "clirr";
  String PLUGIN_NAME = "Clirr";

  String RULE_API_BREAK = "clirr-api-break";
  String RULE_NEW_API = "clirr-new-api";
  String RULE_API_BEHAVIOR_CHANGE = "clirr-api-behavior-change";

  String RESULT_TXT = "clirr-result.txt";
  String COMPARISON_VERSION_PROPERTY = "sonar.clirr.comparisonVersion";

  String API_PROPERTY = "sonar.clirr.api";
}
