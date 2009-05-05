package org.codehaus.sonar.clirrplugin;

import java.io.InputStream;
import java.util.List;

import org.sonar.commons.resources.Resource;
import org.sonar.commons.rules.Rule;
import org.sonar.commons.rules.RuleFailureLevel;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.maven.xml.XpathParser;
import org.w3c.dom.Element;

public class ClirrXmlResultParser {

	//	<?xml version="1.0"?>
	//	<diffreport>
	//	  <difference binseverity="ERROR" srcseverity="ERROR" class="org.example.clirrtest.App" method="public void main(java.lang.String[])">Method 'public void main(java.lang.String[])' wurde entfernt</difference>
	//	</diffreport>
	private final XpathParser parser = new XpathParser();
	private final ProjectContext projectContext;

	public ClirrXmlResultParser(ProjectContext projectContext) {
		this.projectContext = projectContext;
	}

	public int parse(InputStream inputStream) {
		int incompatibilities = 0;
		parser.parse(inputStream);
		List<Element> differences = parser.getChildElements("difference");
		for (Element difference : differences) {
			difference.getAttribute("binseverity");
			difference.getAttribute("srcseverity");
			String className = difference.getAttribute("class");
			difference.getAttribute("method");
			difference.getAttribute("field");

			Resource resource = Java.newClass(className);
			Rule testRule = new ClirrRulesRepository().getInitialReferential().get(0);
			String message = "Sample clirr violation";
			RuleFailureLevel level = RuleFailureLevel.ERROR;
			Integer lineId = Integer.valueOf(1);
			projectContext.addViolation(resource, testRule, message, level, lineId);

			incompatibilities++;
		}
		return incompatibilities;
	}

}
