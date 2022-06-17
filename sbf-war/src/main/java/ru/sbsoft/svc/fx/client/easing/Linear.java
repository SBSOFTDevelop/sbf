/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * A one to one {@link EasingFunction}.
 * 
 */
public class Linear implements EasingFunction {

  @Override
  public double func(double n) {
    return n;
  }

}
