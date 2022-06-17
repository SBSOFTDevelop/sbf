/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

/**
 * Captures details for the Slider field appearance generation
 */
public interface SliderDetails {
  @TypeDetails(sampleValue = "#f5f5f5", comment = "background color for slider track")
  String trackBackgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#d4d4d4', 1)", comment = "border")
  BorderDetails trackBorder();

  @TypeDetails(sampleValue = "8", comment = "height of track, not the entire field")
  int trackHeight();

  @TypeDetails(sampleValue = "4", comment = "border radius for track")
  int trackRadius();



  @TypeDetails(sampleValue = "#f5f5f5", comment = "background color for thumb")
  String thumbBackgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#777777', 1)", comment = "border")
  BorderDetails thumbBorder();

  @TypeDetails(sampleValue = "15", comment = "height of thumb")
  int thumbHeight();

  @TypeDetails(sampleValue = "8", comment = "border radius for thumb")
  int thumbRadius();

  @TypeDetails(sampleValue = "15", comment = "width of thumb")
  int thumbWidth();
}
