/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.clirr.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.sonar.api.web.gwt.client.AbstractPage;
import org.sonar.api.web.gwt.client.ResourceDictionary;
import org.sonar.api.web.gwt.client.webservices.*;

import java.util.Arrays;

public class GwtClirrPage extends AbstractPage {

  public static final String GWT_ID = "org.sonar.plugins.clirr.GwtClirrPage";

  private static final WSMetrics.Metric TOTAL_API_CHANGES = new WSMetrics.Metric("clirr_total_api_changes");
  private static final WSMetrics.Metric API_BREAKS = new WSMetrics.Metric("clirr_api_breaks");
  private static final WSMetrics.Metric API_BEHAVIOR_CHANGES = new WSMetrics.Metric("clirr_api_behavior_changes");
  private static final WSMetrics.Metric NEW_API = new WSMetrics.Metric("clirr_new_api");

  private VerticalPanel panel = new VerticalPanel();

  public void onModuleLoad() {
    displayView(panel);
    String resourceKey = ResourceDictionary.getResourceKey();
    loadMeasures(resourceKey);
    loadViolations(resourceKey);

  }

  private void loadMeasures(String resourceKey) {
    ResourcesQuery.get(resourceKey)
        .setMetrics(Arrays.asList(TOTAL_API_CHANGES, API_BREAKS, API_BEHAVIOR_CHANGES, NEW_API))
        .setDepth(0)
        .execute(new OnMeasuresLoaded());

  }

  private class OnMeasuresLoaded extends BaseQueryCallback<Resources> {
    public void onResponse(Resources resources, JavaScriptObject javaScriptObject) {
      if (resources.firstResource() == null || resources.firstResource().getMeasure(TOTAL_API_CHANGES) == null) {
        displayNoData();

      } else {
        displayMeasures(resources.firstResource());
      }
    }

    private void displayMeasures(Resource resource) {
      panel.add(new HTML("Measures loaded: " + resource.getMeasures().size()));
    }

    private void displayNoData() {
      panel.add(new HTML("No data"));
    }
  }

  private void loadViolations(String resourceKey) {
    ViolationsQuery.create(resourceKey)
        .setDepth(-1)
        .setRules("clirr:clirr-api-break,clirr:clirr-api-behavior-change,clirr:clirr-new-api")
        .execute(new OnViolationsLoaded());
  }


  private class OnViolationsLoaded extends BaseQueryCallback<Violations> {
    public void onResponse(Violations violations, JavaScriptObject jsonRawResponse) {
      panel.add(new HTML("Violations loaded: " + violations.getAll().size()));
    }
  }
}
