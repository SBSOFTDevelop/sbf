/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import ru.sbsoft.svc.core.client.gestures.impl.IsElementImpl;
import ru.sbsoft.svc.core.client.util.Point;

public class TouchData {
  public enum Type {
    Start, Move, End, Cancel
  }


  private final Point startPosition;
  private final int identifier;
  private Point lastPosition;
  private IsElement startElement;
  private Type lastChange = Type.Start;
  private NativeEvent lastNativeEvent;

  public TouchData(Point startPosition, int identifier, IsElement startElement) {
    this.startPosition = startPosition;
    this.lastPosition = startPosition;
    this.identifier = identifier;
    this.startElement = startElement;
  }

  public TouchData(Point startPosition, int identifier, NativeEvent startEvent) {
    this(startPosition, identifier, startEvent.getEventTarget());
    this.lastNativeEvent = startEvent;
  }

  public TouchData(Point startPosition, int identifier, EventTarget eventTarget) {
    this.startPosition = startPosition;
    this.lastPosition = startPosition;
    this.identifier = identifier;
    if (eventTarget != null && GWT.isClient() && Element.is(eventTarget)) {
      this.startElement = eventTarget.<IsElementImpl>cast();
    }
  }

  public IsElement getStartElement() {
    return startElement;
  }

  public Point getStartPosition() {
    return startPosition;
  }

  public int getIdentifier() {
    return identifier;
  }

  public NativeEvent getLastNativeEvent() {
    return lastNativeEvent;
  }

  public void setLastNativeEvent(NativeEvent lastNativeEvent) {
    this.lastNativeEvent = lastNativeEvent;
  }

  public Point getLastPosition() {
    return lastPosition;
  }

  public void setLastPosition(Point lastPosition) {
    this.lastPosition = lastPosition;
  }

  public Type getLastChange() {
    return lastChange;
  }

  public void setLastChange(Type lastChange) {
    this.lastChange = lastChange;
  }
}
