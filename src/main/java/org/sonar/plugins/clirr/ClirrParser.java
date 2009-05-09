package org.sonar.plugins.clirr;

public interface ClirrParser {

	void parse();

	int getIncompatibilities();

}
