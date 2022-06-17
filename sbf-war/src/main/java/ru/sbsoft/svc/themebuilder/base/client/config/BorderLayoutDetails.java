/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface BorderLayoutDetails {

  @TypeDetails(sampleValue = "#DFEAF2", comment = "background for the borderlayoutcontainer, visible in margins and collapsed regions")
  String panelBackgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#DFEAF2', 1)", comment = "border styling for a non-mini collapsed region")
  BorderDetails collapsePanelBorder();

  @TypeDetails(sampleValue = "#157FCC", comment = "background color for the collapsed panels")
  String collapsePanelBackgroundColor();
}
