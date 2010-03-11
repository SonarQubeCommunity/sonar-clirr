package org.sonar.plugins.clirr;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.JavaFile;

public class ClirrViolation {

  private final String messageId;
  private final String message;
  private final JavaFile resource;
  private final String severity;
  private Type type;

  public ClirrViolation(final String severity, final String messageId, final String message, final String affectedClass) {
    this.severity = severity;
    this.resource = getResource(affectedClass);
    this.message = message;
    this.messageId = messageId;
    for (final Type aType : Type.values()) {
      if (aType.getClirrSeverity().equals(severity)) {
        this.type = aType;
      }
    }

  }

  private JavaFile getResource(final String className) {
    if (StringUtils.isBlank(className) || className.indexOf('$') == -1) {
      return new JavaFile(className);
    }
    return new JavaFile(className.substring(0, className.indexOf('$')));
  }

  public JavaFile getJavaFile() {
    return resource;
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
