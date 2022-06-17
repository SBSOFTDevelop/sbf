/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} that at first moves back from the origin.
 */
public class BackIn implements EasingFunction {

  @Override
  public double func(double n) {
    return n * n * ((1.70158 + 1) * n - 1.70158);
  }

}
