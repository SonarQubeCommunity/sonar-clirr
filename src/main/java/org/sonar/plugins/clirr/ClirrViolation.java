package org.sonar.plugins.clirr;

import org.apache.commons.lang.StringUtils;

public class ClirrViolation {

	private final String messageId;
	private final String message;
	private final String affectedClass;
	private final String severity;

	public ClirrViolation(String severity, String messageId, String message, String affectedClass) {
		this.severity = severity;
		this.affectedClass = filterInnerClass(affectedClass);
		this.message = message;
		this.messageId = messageId;
	}

	private String filterInnerClass(String className) {
		if (StringUtils.isBlank(className) || className.indexOf("$") == -1) {
			return className;
		}
		return className.substring(0, className.indexOf("$"));
	}

	public String getSeverity() {
		return severity;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getMessage() {
		return message;
	}

	public String getAffectedClass() {
		return affectedClass;
	}

}
