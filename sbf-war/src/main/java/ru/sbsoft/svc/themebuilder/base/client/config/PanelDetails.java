/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface PanelDetails {

  @TypeDetails(sampleValue = "#ffffff", comment = "background color for the panel body")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "entire panel padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "util.padding(10)", comment = "header padding")
  EdgeDetails headerPadding();

  @TypeDetails(sampleValue = "#ccffff", comment = "background color to fill behind the header gradient")
  String headerBackgroundColor();

  @TypeDetails(sampleValue = "#ccffff, #ccffff", comment = "header gradient string")
  String headerGradient();

  @TypeDetails(sampleValue = "15px", comment = "header Line Height")
  String headerLineHeight();

  @TypeDetails(sampleValue = "util.border('solid', '#000000', 1)", comment = "border around the contentpanel")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "panel heading text style")
  FontDetails font();
}
