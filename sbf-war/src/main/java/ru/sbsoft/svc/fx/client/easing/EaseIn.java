/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} that produces an easing at the start of the animation.
 */
public class EaseIn implements EasingFunction {

  @Override
  public double func(double n) {
    return Math.pow(n, 1.7);
  }

}
