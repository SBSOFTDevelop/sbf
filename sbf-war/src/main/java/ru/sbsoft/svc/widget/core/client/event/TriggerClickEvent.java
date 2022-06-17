/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;

/**
 * Fires after the trigger is clicked.
 */
public class TriggerClickEvent extends GwtEvent<TriggerClickHandler> {

  /**
   * Handler class for {@link TriggerClickEvent} events.
   */
  public interface TriggerClickHandler extends EventHandler {

    /**
     * Called after the source is expanded.
     */
    void onTriggerClick(TriggerClickEvent event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link TriggerClickEvent} events.
   */
  public interface HasTriggerClickHandlers {

    /**
     * Adds a {@link TriggerClickHandler} handler for {@link TriggerClickEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addTriggerClickHandler(TriggerClickHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<TriggerClickHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<TriggerClickHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<TriggerClickHandler>());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<TriggerClickHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(TriggerClickHandler handler) {
    handler.onTriggerClick(this);
  }

}
