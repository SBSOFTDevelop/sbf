/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeCollapseEvent.BeforeCollapseHandler;

/**
 * Fires before an item is collapsed.
 */
public class BeforeCollapseEvent extends GwtEvent<BeforeCollapseHandler> implements CancellableEvent {

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeCollapseEvent} events.
   */
  public interface HasBeforeCollapseHandlers {

    /**
     * Adds a {@link BeforeCollapseHandler} handler for {@link BeforeCollapseEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeCollapseHandler(BeforeCollapseHandler handler);
  }

  
  /**
   * Handler class for {@link BeforeCollapseEvent} events.
   */
  public interface BeforeCollapseHandler extends EventHandler {

    /**
     * Called before a content panel is collapsed. Listeners can cancel the action
     * by calling {@link BeforeCollapseEvent#setCancelled(boolean)}.
     */
    void onBeforeCollapse(BeforeCollapseEvent event);
  }
  
  /**
   * Handler type.
   */
  private static Type<BeforeCollapseHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeCollapseHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BeforeCollapseHandler>());
  }

  private boolean cancelled;

  


  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeCollapseHandler> getAssociatedType() {
    return (Type) TYPE;
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
  protected void dispatch(BeforeCollapseHandler handler) {
    handler.onBeforeCollapse(this);
  }

}
