/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import ru.sbsoft.svc.core.client.dom.XElement;

/**
 * Base <code>Effect</code> implementation for elements.
 */
public class BaseEffect implements Effect {

  protected XElement element;

  protected BaseEffect(XElement element) {
    this.element = element;
  }

  public void onCancel() {

  }

  public void onComplete() {

  }

  public void onStart() {

  }

  public void onUpdate(double progress) {

  }

  protected double getValue(double from, double to, double progress) {
    return (from + ((to - from) * progress));
  }

}
