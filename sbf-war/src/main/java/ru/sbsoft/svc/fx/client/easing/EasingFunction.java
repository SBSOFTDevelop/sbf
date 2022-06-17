/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * Interface for classes that contain an easing equation.
 * 
 */
public interface EasingFunction {

  /**
   * Returns the calculated easing of the passed in n value.
   * 
   * @param n the value to be eased
   * @return the calculated easing
   */
  public double func(double n);

}
