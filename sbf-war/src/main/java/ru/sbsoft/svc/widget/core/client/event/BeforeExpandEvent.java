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
import ru.sbsoft.svc.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;

/**
 * Event type for widgets that can be expanded.
 */
public class BeforeExpandEvent extends GwtEvent<BeforeExpandHandler> implements CancellableEvent {

  /**
   * Handler class for {@link BeforeCollapseEvent} events.
   */
  public interface BeforeExpandHandler extends EventHandler {

    /**
     * Called before a content panel is expanded. Listeners can cancel the action
     * by calling {@link BeforeExpandEvent#setCancelled(boolean)}.
     */
    void onBeforeExpand(BeforeExpandEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeExpandEvent} events.
   */
  public interface HasBeforeExpandHandlers {

    /**
     * Adds a {@link BeforeExpandHandler} handler for {@link BeforeExpandEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeExpandHandler(BeforeExpandHandler handler);
  }
  
  /**
   * Handler type.
   */
  private static Type<BeforeExpandHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeExpandHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BeforeExpandHandler>());
  }

  private boolean cancelled;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeExpandHandler> getAssociatedType() {
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
  protected void dispatch(BeforeExpandHandler handler) {
    handler.onBeforeExpand(this);
  }

}
