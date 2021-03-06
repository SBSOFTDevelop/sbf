/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface InfoDetails {

  @TypeDetails(sampleValue="#000000", comment = "background color")
  String backgroundColor();

  @TypeDetails(sampleValue = "0.8", comment = "popup opacity, 0.0-1.0")
  double opacity();

  @TypeDetails(sampleValue = "util.padding(4)", comment = "padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "6", comment = "border radius")
  int borderRadius();

  @TypeDetails(sampleValue = "util.border('none')", comment = "border parameters")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.radiusMinusBorderWidth(border, borderRadius)", comment = "helper for leftover space in css3 versus sliced images")
  EdgeDetails radiusMinusBorderWidth();

  @TypeDetails(sampleValue = "util.margin(4,0,0,0)", comment = "margin")
  EdgeDetails margin();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','large','#ffffff','bold')", comment = "info header text style")
  FontDetails headerText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#ffffff','normal')", comment = "info message text style")
  FontDetails messageText();

  @TypeDetails(sampleValue = "util.padding(4)", comment = "padding around the header text")
  EdgeDetails headerPadding();

  @TypeDetails(sampleValue = "util.padding(4)", comment = "padding around the message text")
  EdgeDetails messagePadding();
}
