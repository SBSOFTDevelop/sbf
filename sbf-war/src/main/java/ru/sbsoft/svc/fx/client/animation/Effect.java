/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

/**
 * Effect interface defines the lifecyle methods for an effect.
 */
public interface Effect {

  /**
   * Fires after the effect is cancelled.
   */
  public void onCancel();

  /**
   * Fires after the effect is complete.
   */
  public void onComplete();

  /**
   * Fires after the effect is started.
   */
  public void onStart();

  /**
   * Fires after the effect is updated.
   * 
   * @param progress the progress between 0 and 1
   */
  public void onUpdate(double progress);
}
