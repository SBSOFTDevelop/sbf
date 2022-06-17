/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface StatusDetails {

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','small')", comment = "status box text")
  FontDetails text();

  @TypeDetails(sampleValue = "'normal'", comment = "line height of the status widget")
  String lineHeight();

  @TypeDetails(sampleValue = "util.padding(0, 2)", comment = "padding around the status text")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "util.border('solid', '#dddddd #ffffff #ffffff #dddddd', 1)", comment = "status box border, only applies to BoxStatusAppearance")
  BorderDetails border();
}
