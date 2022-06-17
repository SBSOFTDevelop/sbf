/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface WindowDetails extends FramedPanelDetails {
  @Override
  @TypeDetails(sampleValue = "1", comment = "border radius of the framed panel")
  int borderRadius();

  @Override
  @TypeDetails(sampleValue = "util.radiusMinusBorderWidth(border, borderRadius)", comment = "helper for leftover space in css3 versus sliced images")
  EdgeDetails radiusMinusBorderWidth();


  @Override
  @TypeDetails(sampleValue = "#ffffff", comment = "background color for the panel body")
  String backgroundColor();

  @Override
  @TypeDetails(sampleValue = "util.padding(0)", comment = "entire panel padding")
  EdgeDetails padding();

  @Override
  @TypeDetails(sampleValue = "util.padding(10)", comment = "header padding")
  EdgeDetails headerPadding();

  @Override
  @TypeDetails(sampleValue = "#ccffff", comment = "background color to fill behind the header gradient")
  String headerBackgroundColor();

  @Override
  @TypeDetails(sampleValue = "#ccffff, #ccffff", comment = "header gradient string")
  String headerGradient();

  @Override
  @TypeDetails(sampleValue = "15px", comment = "header line height")
  String headerLineHeight();

  @Override
  @TypeDetails(sampleValue = "util.border('solid', '#000000', 1)", comment = "border around the contentpanel")
  BorderDetails border();

  @Override
  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "panel heading text style")
  FontDetails font();
}
