/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface FieldSetDetails {
  @TypeDetails(sampleValue = "#ffffff", comment = "Background color")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.padding(5)", comment = "padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "util.padding(5)", comment = "legend padding")
  EdgeDetails legendPadding();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','large')", comment = "text details for the fieldset's legend")
  FontDetails text();

  @TypeDetails(sampleValue = "util.border('solid', '#bbbbbb', 1)", comment = "border styling and colors around the fieldset")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.5)", comment = "primary color of collapse icon")
  String collapseIconColor();

  @TypeDetails(sampleValue = "#dddddd", comment = "collapse icon color when hovered")
  String collapseOverIconColor();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.5)", comment = "primary color of expand icon")
  String expandIconColor();

  @TypeDetails(sampleValue = "#dddddd", comment = "expand icon color when hovered")
  String expandOverIconColor();
}
