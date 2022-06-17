/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import ru.sbsoft.svc.core.client.dom.XElement;

public class Move extends BaseEffect {

  private int fromX, toX;
  private int fromY, toY;

  public Move(XElement el, int x, int y) {
    super(el);
    el.makePositionable();
    fromX = el.getX();
    fromY = el.getY();

    toX = x;
    toY = y;
  }

  @Override
  public void onComplete() {
    super.onComplete();
    element.setXY(toX, toY);
  }

  @Override
  public void onUpdate(double progress) {
    int x = (int) getValue(fromX, toX, progress);
    int y = (int) getValue(fromY, toY, progress);

    element.setXY(x, y);
  }
}
