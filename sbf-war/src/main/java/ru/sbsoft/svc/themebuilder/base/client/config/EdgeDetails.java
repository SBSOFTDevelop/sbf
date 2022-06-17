/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

import ru.sbsoft.svc.themebuilder.base.client.config.DetailTemplate;

/**
 *
 */
public interface EdgeDetails {
  int top();
  int right();
  int bottom();
  int left();


  @Override
  @DetailTemplate("{top}px {right}px {bottom}px {left}px")
  public String toString();
}
