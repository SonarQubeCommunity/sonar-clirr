package org.sonar.plugins.clirr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ClirrTxtResultParser {

	public List<ClirrViolation> parse(InputStream inputStream, Charset charset) throws IOException {
		List<ClirrViolation> violations = new ArrayList<ClirrViolation>();
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream, charset));
		String line = reader.readLine();
		while (line != null) {
			String[] split = line.split(Pattern.quote(":"));
			if (split.length < 4) {
				line = reader.readLine();
				continue;
			}
			violations.add(parseViolationLine(split));
			line = reader.readLine();
		}
		reader.close();
		return violations;
	}

	private ClirrViolation parseViolationLine(String[] split) {
		ClirrViolation violation = new ClirrViolation(split[0].trim(), split[1].trim(), split[3].trim(), split[2]
				.trim());
		return violation;
	}
}
