/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} that produces an easing at the end of the animation.
 */
public class EaseOut implements EasingFunction {

  @Override
  public double func(double n) {
    return Math.pow(n, 0.48);
  }

}
