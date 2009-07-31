package org.sonar.plugins.clirr;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.JavaFile;

public class ClirrViolation {

  private final String messageId;
  private final String message;
  private final JavaFile resource;
  private final String severity;
  private Type type;

  public ClirrViolation(String severity, String messageId, String message, String affectedClass) {
    this.severity = severity;
    this.resource = getResource(affectedClass);
    this.message = message;
    this.messageId = messageId;
    for (Type type : Type.values()) {
      if (type.getClirrSeverity().equals(severity)) {
        this.type = type;
      }
    }

  }

  private JavaFile getResource(String className) {
    if (StringUtils.isBlank(className) || className.indexOf("$") == -1) {
      return new JavaFile(className);
    }
    return new JavaFile(className.substring(0, className.indexOf("$")));
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

    private Type(String clirrSeverity) {
      this.severity = clirrSeverity;
    }

    public String getClirrSeverity() {
      return severity;
    }

  }
}
