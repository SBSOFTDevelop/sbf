/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.shared.event.GroupingHandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * Adapter to add gesture support to an existing widget without extending it.
 *
 * @TODO support pointer here
 */
public class TouchEventToGestureAdapter implements TouchStartHandler, TouchMoveHandler, TouchEndHandler, TouchCancelHandler {

  private final Widget targetWidget;
  private final GestureRecognizer gestureRecognizer;
  private final GroupingHandlerRegistration handlerRegistration = new GroupingHandlerRegistration();

  /**
   * Add gesture support to an existing widget.
   * If the widget is a SVC Component then handle the events in the component otherwise handle them here.
   *
   * @param targetWidget add gesture support to this widget
   * @param gestureRecongnizer is the type of touch support to be added
   */
  public TouchEventToGestureAdapter(Widget targetWidget, GestureRecognizer gestureRecongnizer) {
    this.targetWidget = targetWidget;
    this.gestureRecognizer = gestureRecongnizer;

    gestureRecongnizer.setDelegate(targetWidget);

    // Handle events in HasGestureRecognizers widgets/components
    if (targetWidget instanceof HasGestureRecognizers) {
      ((HasGestureRecognizers) targetWidget).addGestureRecognizer(gestureRecongnizer);
    } else {
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchStartEvent.getType()));
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchMoveEvent.getType()));
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchEndEvent.getType()));
      handlerRegistration.add(targetWidget.addDomHandler(this, TouchCancelEvent.getType()));
    }
  }

  public HandlerRegistration getHandlerRegistration() {
    return handlerRegistration;
  }

  public Widget getTargetWidget() {
    return targetWidget;
  }

  @Override
  public void onTouchCancel(TouchCancelEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    gestureRecognizer.handle(event.getNativeEvent());
  }

}