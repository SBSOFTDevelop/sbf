/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.easing;

/**
 * {@link EasingFunction} that produces an {@link Elastic} at the end of the
 * animation.
 */
public class ElasticOut extends Elastic {

  @Override
  public double func(double n) {
    return 1 - super.func(1 - n);
  }

}
