/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} to simulate elastic.
 */
public abstract class Elastic implements EasingFunction {

  @Override
  public double func(double n) {
    if (n == 0 || n == 1) {
      return n;
    }
    double p = 0.3, s = p / 4;
    return Math.pow(2, -10 * n) * Math.sin((n - s) * (2 * Math.PI) / p) + 1;
  }
}
