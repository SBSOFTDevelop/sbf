/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import com.google.gwt.animation.client.Animation;
import ru.sbsoft.svc.fx.client.easing.Default;
import ru.sbsoft.svc.fx.client.easing.EasingFunction;

/**
 * Adds additional features and functionality to the {@link Animation} class.
 * 
 * The {@link EasingFunction} replaces the interpolation of the
 * {@link Animation}'s progress.
 */
public abstract class Animator extends Animation {

  private EasingFunction easing = new Default();

  /**
   * Returns the {@link EasingFunction} used in this animation.
   * 
   * @return the {@link EasingFunction} used in this animation
   */
  public EasingFunction getEasing() {
    return easing;
  }

  /**
   * Calls {@link Animation#run(int, double)} and sets the
   * {@link EasingFunction} to be used in the animation.
   */
  public void run(int duration, double startTime, EasingFunction easing) {
    this.easing = easing;
    run(duration, startTime);
  }

  /**
   * Calls {@link Animation#run(int)} and sets the {@link EasingFunction} to be
   * used in the animation.
   */
  public void run(int duration, EasingFunction easing) {
    this.easing = easing;
    run(duration);
  }

  /**
   * Sets the {@link EasingFunction} that the animation will use.
   * 
   * @param easing the {@link EasingFunction} that the animation will use
   */
  public void setEasing(EasingFunction easing) {
    this.easing = easing;
  }

  @Override
  protected double interpolate(double progress) {
    return easing.func(progress);
  }

}
