/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import ru.sbsoft.svc.core.client.dom.XElement;

public class FadeOut extends BaseEffect {

  public FadeOut(XElement el) {
    super(el);
  }

  public void onComplete() {
    element.setVisible(false);
    element.getStyle().setProperty("opacity", "");
  }

  @Override
  public void onUpdate(double progress) {
    element.getStyle().setOpacity(Math.max(1 - progress, 0));
  }

}