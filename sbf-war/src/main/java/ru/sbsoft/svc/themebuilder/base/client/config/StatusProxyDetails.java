/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface StatusProxyDetails {
  @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'normal')", comment = "dnd proxy test styling")
  FontDetails text();

  @TypeDetails(sampleValue = "util.border('solid', '#dddddd #bbbbbb #bbbbbb #dddddd', 1)", comment = "border around the dnd proxy")
  BorderDetails border();

  @TypeDetails(sampleValue = "#ffffff", comment = "background color for the dnd proxy")
  String backgroundColor();

  @TypeDetails(sampleValue = "0.85", comment = "opacity of the status proxy")
  double opacity();
}
