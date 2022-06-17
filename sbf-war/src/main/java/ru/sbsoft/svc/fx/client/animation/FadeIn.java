/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import ru.sbsoft.svc.core.client.dom.XElement;

public class FadeIn extends BaseEffect {

  public FadeIn(XElement el) {
    super(el);
  }

  public void onComplete() {
    element.getStyle().setProperty("opacity", "");
  }

  public void onStart() {
    element.getStyle().setOpacity(0);
    element.setVisible(true);
  }

  @Override
  public void onUpdate(double progress) {
    element.getStyle().setOpacity(progress);
  }

}