package org.sonar.plugins.clirr;

import org.sonar.api.web.AbstractDashboardWidget;

public class ClirrWidget extends AbstractDashboardWidget {

  protected String getTemplatePath() {
    return "/clirr_widget.html.erb";
  }

}