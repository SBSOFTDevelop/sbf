/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Gesture events are logical events - they describe as a gesture occurs, and how it ends
 * (failure/success, or 'end' vs 'cancel'). A gesture canceled event does not necessarily
 * mean that a touch was canceled nor vice versa - a touch cancel event means only that
 * the OS/Browser decided that the touch could not continue or end normally, and a gesture
 * cancel means that the entire gesture did not complete normally.
 * @param <H>
 */
public abstract class AbstractGestureEvent<H extends EventHandler> extends GwtEvent<H> {
  private final GestureRecognizer gesture;

  public AbstractGestureEvent(GestureRecognizer gesture) {
    this.gesture = gesture;
  }

  /**
   * Stops the gesture recognition process, allowing the move events to propagate.
   */
  public void cancel() {
    gesture.cancel();
  }

  public List<TouchData> getActiveTouches() {
    return gesture.getTouches();
  }

}