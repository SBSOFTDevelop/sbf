/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

/**
 *
 */
public interface BorderDetails extends EdgeDetails {
  String style();
  String color();

  @Override
  @DetailTemplate("{top}px {right}px {bottom}px {left}px")
  public String toString();
}
