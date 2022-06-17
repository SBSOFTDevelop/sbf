/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

import ru.sbsoft.svc.themebuilder.base.client.config.TypeDetails;

/**
 *
 */
public interface ToolIconDetails {
  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.5)", comment = "primary color of icons")
  String primaryColor();

  @TypeDetails(sampleValue = "1", comment = "primary opacity of icons")
  double primaryOpacity();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.3)", comment = "icon color when hover")
  String primaryOverColor();

  @TypeDetails(sampleValue = "1", comment = "opacity when hover")
  double primaryOverOpacity();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.25)", comment = "icon color when clicked")
  String primaryClickColor();

  @TypeDetails(sampleValue = "1", comment = "icon opacity when clicked")
  double primaryClickOpacity();

  @TypeDetails(sampleValue = "#ff0000", comment = "color used for warning actions, such as exclamation icon")
  String warningColor();

  @TypeDetails(sampleValue = "#00ff00", comment = "color used for allowed actions, such as allowed drop zones with DnD")
  String allowColor();
}
