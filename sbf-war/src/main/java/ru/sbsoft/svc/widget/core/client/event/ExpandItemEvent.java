/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;

/**
 * Fires after an item is expanded.
 */
public class ExpandItemEvent<T> extends GwtEvent<ExpandItemHandler<T>> {

  /**
   * Handler class for {@link ExpandItemEvent} events.
   */
  public  interface ExpandItemHandler<T> extends EventHandler {

    /**
     * Called after an item is expanded.
     */
    void onExpand(ExpandItemEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link ExpandItemEvent} events.
   */
  public interface HasExpandItemHandlers<T> {

    /**
     * Adds a {@link ExpandItemHandler} handler for {@link ExpandItemEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addExpandHandler(ExpandItemHandler<T> handler);
  }
  
  /**
   * Handler type.
   */
  private static Type<ExpandItemHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ExpandItemHandler<?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<ExpandItemHandler<?>>());
  }
  
  private T item;
  
  public ExpandItemEvent(T item) {
    this.item = item;
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<ExpandItemHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }
  
  public T getItem() {
    return item;
  }

  @Override
  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(ExpandItemHandler<T> handler) {
    handler.onExpand(this);
  }

}
