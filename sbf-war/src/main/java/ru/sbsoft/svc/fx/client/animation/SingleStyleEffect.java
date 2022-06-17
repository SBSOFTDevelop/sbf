/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import ru.sbsoft.svc.core.client.dom.XElement;

public class SingleStyleEffect extends BaseEffect {
  /**
   * The css style be adjusted.
   */
  public String style;

  /**
   * The start value.
   */
  public double from;

  /**
   * The end value.
   */
  public double to;

  public SingleStyleEffect(XElement el) {
    super(el);
  }

  public SingleStyleEffect(XElement el, String style, double from, double to) {
    this(el);
    this.style = style;
    this.from = from;
    this.to = to;
  }

  public void increase(double value) {
    element.getStyle().setProperty(style, String.valueOf(value));
  }

  public void onUpdate(double progress) {
    increase(getValue(from, to, progress));
  }

}
