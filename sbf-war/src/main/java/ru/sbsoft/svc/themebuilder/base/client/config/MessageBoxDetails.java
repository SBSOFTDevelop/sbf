/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface MessageBoxDetails {
  @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'normal')", comment = "style of the text in the body of the message box")
  FontDetails text();

  @TypeDetails(sampleValue = "util.padding(10, 10, 5)", comment = "padding around the message box")
  EdgeDetails messagePadding();

  @TypeDetails(sampleValue = "util.padding(5, 10, 10)", comment = "padding around the body of the message box")
  EdgeDetails bodyPadding();

  @TypeDetails(sampleValue = "util.padding(10)", comment = "padding around the icon")
  EdgeDetails iconPadding();
}
