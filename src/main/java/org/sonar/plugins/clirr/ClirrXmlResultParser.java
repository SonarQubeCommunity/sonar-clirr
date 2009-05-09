package org.sonar.plugins.clirr;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.sonar.commons.resources.Resource;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.model.MavenPom;
import org.sonar.plugins.api.maven.xml.XpathParser;
import org.sonar.plugins.api.rules.RulesManager;
import org.w3c.dom.Element;

public final class ClirrXmlResultParser extends AbstractResultParser {

	//	<?xml version="1.0"?>
	//	<diffreport>
	//	  <difference binseverity="ERROR" srcseverity="ERROR" class="org.example.clirrtest.App" method="public void main(java.lang.String[])">Method 'public void main(java.lang.String[])' wurde entfernt</difference>
	//	</diffreport>
	private final XpathParser parser = new XpathParser();

	public ClirrXmlResultParser(MavenPom pom, ProjectContext projectContext, RulesManager rulesManager) {
		super(pom, projectContext, rulesManager);
	}

	@Override
	protected String getFilenameToParse() {
		return ClirrPlugin.CLIRR_RESULT_XML;
	}

	@Override
	public void parse(InputStream inputStream) {
		parser.parse(inputStream);
		List<Element> differences = parser.getChildElements("difference");
		for (Element difference : differences) {
			// difference.getAttribute("method");
			// difference.getAttribute("field");
			String binSeverity = difference.getAttribute("binseverity");
			//			String srcSeverity = difference.getAttribute("srcseverity");
			String className = difference.getAttribute("class");
			String messageId = "0"; // XML is missing MessageIds
			String message = difference.getTextContent();
			Resource resource = Java.newClass(className);
			createViolation(binSeverity, messageId, message, resource);
		}
	}

	@Override
	protected File getFileToParse() {
		return new File(getMavenPom().getBuildDir(), getFilenameToParse());
	}

}
