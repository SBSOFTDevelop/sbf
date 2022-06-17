/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;

/**
 * Fires after an item is collapsed.
 */
public class CollapseItemEvent<T> extends GwtEvent<CollapseItemHandler<T>> {

  /**
   * Handler class for {@link CollapseItemEvent} events.
   */
  public interface CollapseItemHandler<T> extends EventHandler {

    /**
     * Called after a panel is collapsed.
     */
    void onCollapse(CollapseItemEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link CollapseItemEvent} events.
   */
  public interface HasCollapseItemHandlers<T> {

    /**
     * Adds a {@link CollapseItemHandler} handler for {@link CollapseItemEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addCollapseHandler(CollapseItemHandler<T> handler);
  }
  
  /**
   * Handler type.
   */
  private static Type<CollapseItemHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CollapseItemHandler<?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<CollapseItemHandler<?>>());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<CollapseItemHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }
  
  private T item;
  
  public CollapseItemEvent(T item) {
    this.item = item;
  }
  
  public T getItem() {
    return item;
  }

  @Override
  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(CollapseItemHandler<T> handler) {
    handler.onCollapse(this);
  }

}
