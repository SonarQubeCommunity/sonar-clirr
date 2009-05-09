package org.sonar.plugins.clirr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.sonar.commons.resources.Resource;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.model.MavenPom;
import org.sonar.plugins.api.rules.RulesManager;

/**
 * Parses a Clirr result file in text format.
 * 
 * <p>
 * The file is written by net.sf.clirr.core.PlainDiffListener and uses a colon
 * separated format: <tt>Severity[: MessageId]: AffectedClass: Message</tt>
 * 
 * <p>
 * <tt>Severity</tt> is one of ERROR, WARNING or INFO. The <tt>MessageId</tt> is
 * a numeric value as defined in Clirr using net.sf.clirr.core.Message objects.
 * <tt>AffectedClass</tt> is the full qualified binary class name.
 * 
 * @author mhaller
 */
public final class ClirrTxtResultParser extends AbstractResultParser {

	private static final int FRAGMENTS = 4;
	private static final int FRAGMENT_SEVERITY = 0;
	private static final int FRAGMENT_MESSAGEID = 1;
	private static final int FRAGMENT_AFFECTED_CLASS = 2;
	private static final int FRAGMENT_MESSAGE = 3;

	public ClirrTxtResultParser(MavenPom pom, ProjectContext projectContext, RulesManager rulesManager) {
		super(pom, projectContext, rulesManager);
	}

	@Override
	protected String getFilenameToParse() {
		return ClirrPlugin.CLIRR_RESULT_TXT;
	}

	@Override
	public void parse(InputStream inputStream) {
		Charset charset = Charset.defaultCharset();
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new InputStreamReader(inputStream, charset));
			parseLines(reader);
		} finally {
			ClirrPlugin.safeClose(reader);
		}
	}

	private void parseLines(LineNumberReader reader) {
		try {
			String line = reader.readLine();
			while (line != null) {
				parseLine(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(getClass()).error("Clirr result file could not be read", e);
		}
	}

	private boolean parseLine(String line) {
		String[] split = line.split(Pattern.quote(":"));
		if (split.length < FRAGMENTS) {
			return false;
		}
		String severity = split[FRAGMENT_SEVERITY].trim();
		String messageId = split[FRAGMENT_MESSAGEID].trim();
		String affectedClassName = split[FRAGMENT_AFFECTED_CLASS].trim();
		String message = split[FRAGMENT_MESSAGE].trim();
		Resource resource = Java.newClass(affectedClassName);
		if (createViolation(severity, messageId, message, resource)) {
			return true;
		}
		return false;
	}

	@Override
	protected File getFileToParse() {
		return new File(getMavenPom().getBuildDir(), getFilenameToParse());
	}

}
