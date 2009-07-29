package org.sonar.plugins.clirr;

import org.sonar.api.web.DashboardWidget;

public class ClirrWidget implements DashboardWidget {

	public String getTemplate() {
		return "/clirrWidget.erb";
	}

}
