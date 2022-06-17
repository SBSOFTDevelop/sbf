/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} that produces a {@link Bounce} at the start of the
 * animation.
 */
public class BounceIn extends Bounce {

  @Override
  public double func(double n) {
    return 1 - super.func(1 - n);
  }

}
