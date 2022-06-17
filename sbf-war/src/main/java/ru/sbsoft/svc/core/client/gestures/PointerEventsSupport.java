/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import ru.sbsoft.svc.core.client.dom.XElement;

/**
 * Adapter to support Pointer event registration
 */
public class PointerEventsSupport {

  public static final PointerEventsSupport impl = GWT.create(PointerEventsSupport.class);

  /**
   * Sink pointer events to given Element.
   * <p/>
   * Makes the assumption that the DOMImpl has already initialized the EventSystem.
   *
   * @param element
   */
  public void sinkPointerEvents(XElement element) {
  }

  /**
   * Determines if the provided event is Pointer originated
   *
   * @param event
   * @return true if Pointer event
   */
  public boolean isPointerEvent(NativeEvent event) {
    return PointerEvents.isPointerEvent(event.getType());
  }

  /**
   * Assigns a pointer (by pointerId) to a specific Element.
   * <p>
   * Unlike Touch events, the event.target changes with pointer events
   * </p>
   *
   * @param element
   * @param event
   */
  public void setPointerCapture(XElement element, NativeEvent event) {
  }

  /**
   * Determines if the incoming NativeEvent is a pointer event with type "touch" or "pen"
   *
   * @param event
   * @return true if event is "pointer" AND one of "touch" or "pen" type
   */
  public boolean isPointerTouchEvent(NativeEvent event) {
    if (!isPointerEvent(event)) {
      return false;
    }
    final PointerType pointerType = PointerType.getPointerType(getPointerType(event));
    return PointerType.TOUCH == pointerType || PointerType.PEN == pointerType;
  }

  /**
   * Identifies whether pointer events are supported in the current running browser.
   *
   * @return true if pointers are supported
   */
  public boolean isSupported() {
    return false;
  }

  /**
   * Converts data in the event to Touch data structure
   *
   * @param event
   * @return JsArray of Touch objects - empty if pointers are not supported
   */
  public JsArray<Touch> getChangedTouches(NativeEvent event) {
    return JsArray.createArray().cast();
  }

  private static native String getPointerType(NativeEvent event) /*-{
    return event.pointerType;
  }-*/;
}
