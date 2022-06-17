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
import ru.sbsoft.svc.widget.core.client.event.BeforeCollapseItemEvent.BeforeCollapseItemHandler;

/**
 * Fires before an item is collapsed.
 */
public class BeforeCollapseItemEvent<T> extends GwtEvent<BeforeCollapseItemHandler<T>> implements CancellableEvent {

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeCollapseItemEvent} events.
   */
  public interface HasBeforeCollapseItemHandlers<T> {

    /**
     * Adds a {@link BeforeCollapseItemHandler} handler for {@link BeforeCollapseItemEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeCollapseHandler(BeforeCollapseItemHandler<T> handler);
  }

  
  /**
   * Handler class for {@link BeforeCollapseItemEvent} events.
   */
  public interface BeforeCollapseItemHandler<T> extends EventHandler {

    /**
     * Called before a content panel is collapsed. Listeners can cancel the action
     * by calling {@link BeforeCollapseItemEvent#setCancelled(boolean)}.
     */
    void onBeforeCollapse(BeforeCollapseItemEvent<T> event);
  }
  
  /**
   * Handler type.
   */
  private static Type<BeforeCollapseItemHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeCollapseItemHandler<?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BeforeCollapseItemHandler<?>>());
  }

  private boolean cancelled;
  private T item;
  
  public BeforeCollapseItemEvent(T item) {
    this.item = item;
  }
  
  public T getItem() {
    return item;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeCollapseItemHandler<T>> getAssociatedType() {
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
  protected void dispatch(BeforeCollapseItemHandler<T> handler) {
    handler.onBeforeCollapse(this);
  }

}
