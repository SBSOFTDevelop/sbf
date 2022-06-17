/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

/**
 * A widget that implements this interface contains GestureRecognizers
 */
public interface HasGestureRecognizers {

  /**
   * Adds a GestureRecognizer.
   *
   * @param gestureRecognizer
   */
  void addGestureRecognizer(GestureRecognizer gestureRecognizer);

  /**
   * Get GestureRecognizer at given index
   *
   * @param index
   * @return GestureRecognizer at given index, null if no GestureRecognizers available
   */
  GestureRecognizer getGestureRecognizer(int index);

  /**
   * Provides the count of GestureRecognizers
   *
   * @return count
   */
  int getGestureRecognizerCount();
}
