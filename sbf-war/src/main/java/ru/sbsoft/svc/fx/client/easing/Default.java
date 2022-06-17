/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * The default {@link EasingFunction} for animation.
 */
public class Default implements EasingFunction {

  @Override
  public double func(double n) {
    return (1 + Math.cos(Math.PI + n * Math.PI)) / 2;
  }

}
