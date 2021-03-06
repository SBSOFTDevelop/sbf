/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;

/**
 * Fires before a selection is made.
 */
public class BeforeSelectEvent extends GwtEvent<BeforeSelectHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<BeforeSelectHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeSelectHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<BeforeSelectHandler>();
    }
    return TYPE;
  }

  private boolean cancelled;
  private Context context;

  public BeforeSelectEvent() {

  }

  public BeforeSelectEvent(Context context) {
    this.context = context;
  }

  public Context getContext() {
    return context;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeSelectHandler> getAssociatedType() {
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
  protected void dispatch(BeforeSelectHandler handler) {
    handler.onBeforeSelect(this);
  }
  
  /**
   * Handler class for {@link BeforeSelectEvent} events.
   */
  public interface BeforeSelectHandler extends EventHandler {

    /**
     * Called before a widget is selected. Listeners can cancel the action by
     * calling {@link BeforeExpandEvent#setCancelled(boolean)}.
     */
    void onBeforeSelect(BeforeSelectEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeSelectEvent} events.
   */
  public interface HasBeforeSelectHandlers {

    /**
     * Adds a {@link BeforeSelectHandler} handler for {@link BeforeSelectEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeSelectHandler(BeforeSelectHandler handler);
  }

}
