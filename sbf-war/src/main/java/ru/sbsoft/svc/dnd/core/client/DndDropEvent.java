/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.dnd.core.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.dnd.core.client.DND.Operation;
import ru.sbsoft.svc.dnd.core.client.DndDropEvent.DndDropHandler;
import ru.sbsoft.svc.fx.client.DragEndEvent;

public class DndDropEvent extends GwtEvent<DndDropHandler> {

  /**
   * Handler type.
   */
  private static Type<DndDropHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<DndDropHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<DndDropHandler>();
    }
    return TYPE;
  }

  private Widget target;
  private DragEndEvent dragEndEvent;
  private StatusProxy statusProxy;
  private Object data;
  private DropTarget dropTarget;
  private Operation operation;

  public DndDropEvent(Widget target, DragEndEvent event, StatusProxy proxy, Object data) {
    this.target = target;
    this.dragEndEvent = event;
    this.statusProxy = proxy;
    this.data = data;
  }

  @Override
  public Type<DndDropHandler> getAssociatedType() {
    return TYPE;
  }

  public Object getData() {
    return data;
  }

  public DragEndEvent getDragEndEvent() {
    return dragEndEvent;
  }

  public DropTarget getDropTarget() {
    return dropTarget;
  }

  public Operation getOperation() {
    return operation;
  }

  public StatusProxy getStatusProxy() {
    return statusProxy;
  }

  public Widget getTarget() {
    return target;
  }

  @Override
  protected void dispatch(DndDropHandler handler) {
    handler.onDrop(this);
  }

  void setDropTarget(DropTarget dropTarget) {
    this.dropTarget = dropTarget;
  }

  void setOperation(Operation operation) {
    this.operation = operation;
  }
  
  public interface DndDropHandler extends EventHandler {

    void onDrop(DndDropEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link DndDropEvent} events.
   */
  public interface HasDndDropHandlers {

    /**
     * Adds a {@link DndDropHandler} handler for {@link DndDropEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addDropHandler(DndDropHandler handler);

  }

}
