/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

/**
 *
 */
public interface TreeDetails {

  @TypeDetails(sampleValue = "util.margin(0)", comment = "checkbox icon margin")
  EdgeDetails checkboxMargin();

  @TypeDetails(sampleValue = "#e2eff8", comment = "tree node drag over background color")
  String dragOverBackgroundColor();

  @TypeDetails(sampleValue = "#e2eff8", comment = "tree node drop background color")
  String dropBackgroundColor();

  @TypeDetails(sampleValue = "util.margin(4, 3, 0, 2)", comment = "margins for tree element icon")
  EdgeDetails iconMargin();

  @TypeDetails(sampleValue = "25px", comment = "tree item height")
  String itemHeight();

  @TypeDetails(sampleValue = "util.padding(0, 6)", comment = "tree node padding")
  EdgeDetails nodePadding();

  @TypeDetails(sampleValue = "#e2eff8", comment = "tree node over background color")
  String overBackgroundColor();

  @TypeDetails(sampleValue = "#c1ddf1", comment = "tree node selected background color")
  String selectedBackgroundColor();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'large','#ffffff','bold')", comment = "tree node text style")
  FontDetails text();
}
