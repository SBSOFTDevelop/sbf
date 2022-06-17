/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

/**
 *
 */
public interface ToolBarDetails {
  @TypeDetails(sampleValue = "#ffffff", comment = "background color of the toolbar for browsers that dont support gradients")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "background gradient of the toolbar")
  String gradient();

  @TypeDetails(sampleValue = "util.border('none')", comment = "border around the toolbar")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.border('none')", comment = "border around the separator")
  BorderDetails separatorBorder();

  @TypeDetails(sampleValue = "14", comment = "height of the separator")
  int separatorHeight();

  @TypeDetails(sampleValue = "util.padding(2)", comment = "padding between the toolbar's border and its contents")
  EdgeDetails padding();

  ButtonDetails buttonOverride();

  LabelToolItemDetails labelItem();

  public interface LabelToolItemDetails {
    @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'medium')", comment = "LabelToolItem text styling")
    FontDetails text();
    @TypeDetails(sampleValue = "'medium'", comment = "LabelToolItem text styling")
    String lineHeight();
    @TypeDetails(sampleValue = "util.padding(2, 2, 0)", comment = "label padding")
    EdgeDetails padding();

  }
}
