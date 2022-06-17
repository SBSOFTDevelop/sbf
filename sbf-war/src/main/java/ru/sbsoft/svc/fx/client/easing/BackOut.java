/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} that moves past the destination before reaching it.
 */
public class BackOut implements EasingFunction {

  @Override
  public double func(double n) {
    n = n - 1;
    return n * n * ((1.70158 + 1) * n + 1.70158) + 1;
  }

}
