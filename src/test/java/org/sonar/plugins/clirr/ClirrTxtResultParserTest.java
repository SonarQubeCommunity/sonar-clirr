package org.sonar.plugins.clirr;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.codehaus.plexus.util.StringInputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.commons.rules.Rule;
import org.sonar.commons.rules.RuleFailureLevel;
import org.sonar.plugins.api.Java;
import org.sonar.plugins.api.maven.ProjectContext;
import org.sonar.plugins.api.rules.RulesManager;
import org.sonar.plugins.clirr.ClirrTxtResultParser;

public class ClirrTxtResultParserTest {

	private ProjectContext context;
	private ClirrTxtResultParser parser;

	@Before
	public void before() throws Exception {
		context = mock(ProjectContext.class);
		RulesManager rulesManager = mock(RulesManager.class);
		when(rulesManager.getPluginRule(anyString(), anyString())).thenAnswer(new Answer<Rule>() {
			public Rule answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				return new Rule((String) args[1], (String) args[1], null, (String) args[0], "");
			}
		});
		parser = new ClirrTxtResultParser(null, context, rulesManager);
	}

	@Test
	public void shouldParseValidClirrResult() {
		parser.parse(new StringInputStream("ERROR: 3003: org.example.MyClass: Added final modifier"));
		verify(context).addViolation(eq(Java.newClass("org.example.MyClass")), any(Rule.class),
				eq("Added final modifier"), eq(RuleFailureLevel.ERROR), eq(Integer.valueOf(1)));
	}

	@Test
	public void shouldIgnoreLinesMissingMessageId() {
		parser.parse(new StringInputStream("ERROR: org.example.MyClass: Added final modifier"));
		verifyNoMoreInteractions(context);
	}

	@Test
	public void shouldEmptyLines() {
		parser.parse(new StringInputStream(""));
		verifyNoMoreInteractions(context);
	}

	@Test
	public void shouldIgnoreCorruptLines() {
		parser.parse(new StringInputStream(":::"));
		verifyNoMoreInteractions(context);
	}

}
