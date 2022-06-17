/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * External api for mangling touch events into gestures (events or callbacks). The handle() method accepts
 * incoming dom events, getTouches() returns the current set of touches this gesture is handling, and cancel()
 * releases the current touches so that other handlers can deal with them.
 * <p/>
 * Various implementations of this can either support on- methods or events to indicate that touch events or
 * gestures are ongoing or have completed. Events are generally expected to be fired from any provided delegate
 * rather than this object itself
 */
public interface GestureRecognizer {
  /**
   * Ends recognition of the gesture, and fires any cancel events that apply. Any touch events
   * being recognized by this object will now be ignored.
   */
  void cancel();

  /**
   * Gets all actively recognized touches that are being recognized by this gesture.
   *
   * @return
   */
  List<TouchData> getTouches();

  /**
   * Takes a browser event, and checks to see if it should decipher a gesture from it.
   * <p/>
   * Callers of this are expected to do their own vetting to decide if the given event should be handled by this
   * gesture - for example, if a cell accepts a tap on one part of it and a longpress on another, the cell should
   * determine where the touch events occur and send to the right recognizer accordingly.
   *
   * @param event the browser event to read for a gesture.
   * @return true if the gesture is not handling the event and can allow it to be propagated, false to indicate that
   * it has been handled and should not be given to other handlers. Should always return true for any start
   * event.
   */
  boolean handle(NativeEvent event);

  /**
   * Sets delegate to receive Gesture events
   *
   * @param eventDelegate
   */
  void setDelegate(HasHandlers eventDelegate);

  /**
   * Artificially starts a gesture
   *
   * @param touches
   */
  void start(List<TouchData> touches);
}