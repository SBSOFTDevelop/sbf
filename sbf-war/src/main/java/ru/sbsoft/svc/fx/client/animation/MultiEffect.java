/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import java.util.ArrayList;
import java.util.List;

import ru.sbsoft.svc.core.client.dom.XElement;

public class MultiEffect extends BaseEffect {

  protected List<Effect> effects;

  public MultiEffect() {
    this(null);
  }

  public MultiEffect(XElement el) {
    super(el);
    effects = new ArrayList<Effect>();
  }

  public void addEffects(Effect... effects) {
    for (int i = 0; i < effects.length; i++) {
      this.effects.add(effects[i]);
    }
  }

  public void addEffects(List<Effect> effects) {
    for (Effect e : effects) {
      this.effects.add(e);
    }
  }

  @Override
  public void onCancel() {
    for (Effect e : effects) {
      e.onCancel();
    }
  }

  @Override
  public void onComplete() {
    for (Effect e : effects) {
      e.onComplete();
    }
  }

  @Override
  public void onStart() {
    for (Effect e : effects) {
      e.onStart();
    }
  }

  @Override
  public void onUpdate(double progress) {
    for (Effect e : effects) {
      e.onUpdate(progress);
    }
  }

}
