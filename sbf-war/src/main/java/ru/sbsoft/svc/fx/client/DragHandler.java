/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.fx.client.DragCancelEvent.DragCancelHandler;
import ru.sbsoft.svc.fx.client.DragEndEvent.DragEndHandler;
import ru.sbsoft.svc.fx.client.DragMoveEvent.DragMoveHandler;
import ru.sbsoft.svc.fx.client.DragStartEvent.DragStartHandler;

public interface DragHandler extends DragStartHandler, DragEndHandler, DragCancelHandler, DragMoveHandler {

  /**
   * A widget that implements this interface is a public source of
   * {@link DragStartEvent}, {@link DragEndEvent}, {@link DragCancelEvent},
   * {@link DragMoveEvent} events.
   */
  public interface HasDragHandlers {

    /**
     * Adds a {@link DragHandler} handler for {@link DragStartEvent} ,
     * {@link DragEndEvent}, {@link DragCancelEvent}, {@link DragMoveEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addDragHandler(DragHandler handler);
  }
}
