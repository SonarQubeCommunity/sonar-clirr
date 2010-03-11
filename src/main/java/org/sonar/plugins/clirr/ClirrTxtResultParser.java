package org.sonar.plugins.clirr;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ClirrTxtResultParser {

	private static final int FIELDCOUNT = 4;
	private static final int IDX_SEVERITY = 0;
	private static final int IDX_ERRCODE = 1;
	private static final int IDX_CLASS = 2;
	private static final int IDX_MESSAGE = 3;

	public List<ClirrViolation> parse(final InputStream input, final Charset charset)
			throws IOException {
		return parse(new InputStreamReader(input, charset));
	}

	public List<ClirrViolation> parse(final Reader input) throws IOException {
		List<ClirrViolation> violations = new ArrayList<ClirrViolation>();
		List<String> lines = IOUtils.readLines(input);
		for (String line : lines) {
			String[] split = line.split(Pattern.quote(":"));
			if (split.length >= FIELDCOUNT) {
				violations.add(parseViolationLine(split));
			}
		}
		return violations;
	}

	private ClirrViolation parseViolationLine(String[] split) {
		return new ClirrViolation(split[IDX_SEVERITY].trim(), split[IDX_ERRCODE].trim(),
				split[IDX_MESSAGE].trim(), split[IDX_CLASS].trim());
	}
}
