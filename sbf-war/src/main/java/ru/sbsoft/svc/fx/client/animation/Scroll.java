/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import ru.sbsoft.svc.core.client.Style.ScrollDir;
import ru.sbsoft.svc.core.client.dom.XElement;

public class Scroll extends SingleStyleEffect {

  protected ScrollDir dir;

  public Scroll(XElement element, ScrollDir dir, int value) {
    super(element);
    this.dir = dir;
    if (dir == ScrollDir.HORIZONTAL) {
      from = element.getScrollLeft();
      to = value;
    } else if (dir == ScrollDir.VERTICAL) {
      from = element.getScrollTop();
      to = value;
    }
  }

  @Override
  public void increase(double value) {
    if (dir == ScrollDir.HORIZONTAL) {
      element.setScrollLeft((int) value);
    } else if (dir == ScrollDir.VERTICAL) {
      element.setScrollTop((int) value);
    }

  }

  @Override
  public void onComplete() {
    super.onComplete();
    if (dir == ScrollDir.HORIZONTAL) {
      element.setScrollLeft((int) to);
    } else if (dir == ScrollDir.VERTICAL) {
      element.setScrollTop((int) to);
    }
  }

}