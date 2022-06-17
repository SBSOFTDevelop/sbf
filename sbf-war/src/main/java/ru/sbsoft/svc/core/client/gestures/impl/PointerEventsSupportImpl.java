/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.impl.DOMImpl;
import com.google.gwt.user.client.impl.DOMImplStandard;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.gestures.PointerEvents;
import ru.sbsoft.svc.core.client.gestures.PointerEventsSupport;

/**
 * Base implementation. Verifies that the DOMIMpl we get is some subclass of DOMImplStandard
 * in order to ride on the same event system
 */
public class PointerEventsSupportImpl extends PointerEventsSupport {
  static final PointerEventsSupport impl = GWT.create(PointerEventsSupport.class);
  static final DOMImpl domImpl = GWT.create(DOMImpl.class);
  static final boolean isDomImplStandard = (domImpl instanceof DOMImplStandard);

  static {
    if (isDomImplStandard) {
      JavaScriptObject eventDispatchers = JavaScriptObject.createObject();
      for (PointerEvents pointerEvent : PointerEvents.values()) {
        addPointerEventDispatcher(eventDispatchers, pointerEvent.getEventName());
      }
      DOMImplStandard.addCaptureEventDispatchers(eventDispatchers);
    }
  }

  private static native void addPointerEventDispatcher(JavaScriptObject eventDispatchers, String eventType) /*-{
    eventDispatchers[eventType] = $entry(@com.google.gwt.user.client.impl.DOMImplStandard::dispatchCapturedEvent(*));
  }-*/;

  private static native void sinkPointerEventsImpl(Element element, String eventListener) /*-{
    element[eventListener] = $entry(@com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent(*));
  }-*/;

  @Override
  public boolean isSupported() {
    return true;
  }

  @Override
  public native void setPointerCapture(XElement element, NativeEvent event)/*-{
    element.setPointerCapture(event.pointerId);
  }-*/;

  @Override
  public native JsArray<Touch> getChangedTouches(NativeEvent event) /*-{
    var touch = {
      identifier: event.pointerId,
      clientX: event.clientX,
      clientY: event.clientY,
      pageX: event.pageX,
      pageY: event.pageY,
      screenX: event.screenX,
      screenY: event.screenY,
      target: event.target
    }

    return [touch];
  }-*/;

  @Override
  public void sinkPointerEvents(XElement element) {
    if (domImpl instanceof DOMImplStandard) {
      for (PointerEvents pointerEvent : PointerEvents.values()) {
        String eventListener = "on" + pointerEvent.getEventName().toLowerCase();
        sinkPointerEventsImpl(element, eventListener);
      }
    }
  }
}
