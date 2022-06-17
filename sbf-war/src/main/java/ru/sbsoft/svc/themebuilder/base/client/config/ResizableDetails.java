/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

public interface ResizableDetails {
  @Optional(defaultValue = "6", comment = "size of resizable drag handle")
  int handleSize();

  @Optional(defaultValue = "-4", comment = "offset for centering around widget borders")
  int handleOffset();
}
