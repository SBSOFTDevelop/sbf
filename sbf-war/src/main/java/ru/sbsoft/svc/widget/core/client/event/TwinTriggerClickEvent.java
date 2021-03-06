/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.TwinTriggerClickEvent.TwinTriggerClickHandler;

/**
 * Fires after the twin trigger is clicked.
 */
public class TwinTriggerClickEvent extends GwtEvent<TwinTriggerClickHandler> {

  /**
   * Handler class for {@link TwinTriggerClickEvent} events.
   */
  public interface TwinTriggerClickHandler extends EventHandler {

    /**
     * Called after the source is expanded.
     */
    void onTwinTriggerClick(TwinTriggerClickEvent event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link TwinTriggerClickEvent} events.
   */
  public interface HasTwinTriggerClickHandlers {

    /**
     * Adds a {@link TwinTriggerClickHandler} handler for {@link TwinTriggerClickEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addTwinTriggerClickHandler(TwinTriggerClickHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<TwinTriggerClickHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<TwinTriggerClickHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<TwinTriggerClickHandler>());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<TwinTriggerClickHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(TwinTriggerClickHandler handler) {
    handler.onTwinTriggerClick(this);
  }

}
