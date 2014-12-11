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

import org.sonar.api.utils.SonarException;

public class ClirrViolation {

  private final String messageId;
  private final String message;
  private final String severity;
  private Type type;
  private final String affectedClass;

  public ClirrViolation(final String severity, final String messageId, final String message, final String affectedClass) {
    this.severity = severity;
    this.message = message;
    this.messageId = messageId;
    this.affectedClass = affectedClass;
    for (final Type aType : Type.values()) {
      if (aType.getClirrSeverity().equals(severity)) {
        this.type = aType;
      }
    }
  }

  public String getAffectedClass() {
    return affectedClass;
  }

  public String getSeverity() {
    return severity;
  }

  public Type getType() {
    return type;
  }

  public String getMessageId() {
    return messageId;
  }

  public String getMessage() {
    return message;
  }

  public String getRuleKey() {
    if (type == Type.BREAK) {
      return ClirrConstants.RULE_API_BREAK;
    }
    if (type == Type.BEHAVIOR_CHANGE) {
      return ClirrConstants.RULE_API_BEHAVIOR_CHANGE;
    }
    if (type == Type.NEW_API) {
      return ClirrConstants.RULE_NEW_API;
    }
    throw new SonarException(String.format("There is no Clirr rule associated to severity '%s'", type));
  }

  public enum Type {
    BREAK("ERROR"), BEHAVIOR_CHANGE("WARNING"), NEW_API("INFO");

    private String severity;

    private Type(final String clirrSeverity) {
      this.severity = clirrSeverity;
    }

    public String getClirrSeverity() {
      return severity;
    }

  }
}
