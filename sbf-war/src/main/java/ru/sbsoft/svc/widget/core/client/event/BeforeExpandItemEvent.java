/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.BeforeExpandItemEvent.BeforeExpandItemHandler;

/**
 * Event type for widgets that can be expanded.
 * 
 * @param <T> the type about to be expanded
 */
public class BeforeExpandItemEvent<T> extends GwtEvent<BeforeExpandItemHandler<T>> implements CancellableEvent {

  /**
   * Handler class for {@link BeforeCollapseEvent} events.
   */
  public interface BeforeExpandItemHandler<T> extends EventHandler {

    /**
     * Called before a content panel is expanded. Listeners can cancel the action
     * by calling {@link BeforeExpandItemEvent#setCancelled(boolean)}.
     */
    void onBeforeExpand(BeforeExpandItemEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeExpandItemEvent} events.
   */
  public interface HasBeforeExpandItemHandlers<T> {

    /**
     * Adds a {@link BeforeExpandItemHandler} handler for {@link BeforeExpandItemEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeExpandHandler(BeforeExpandItemHandler<T> handler);
  }
  
  /**
   * Handler type.
   */
  private static Type<BeforeExpandItemHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeExpandItemHandler<?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BeforeExpandItemHandler<?>>());
  }

  private boolean cancelled;
  private T item;
  
  public BeforeExpandItemEvent(T item) {
    this.item = item;
  }
  
  public T getItem() {
    return item;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeExpandItemHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(BeforeExpandItemHandler<T> handler) {
    handler.onBeforeExpand(this);
  }

}
