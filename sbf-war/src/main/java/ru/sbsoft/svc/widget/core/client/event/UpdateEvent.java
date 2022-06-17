/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.UpdateEvent.UpdateHandler;

/**
 * Fires when the source is updated.
 */
public class UpdateEvent extends GwtEvent<UpdateHandler> {

  /**
   * A widget that implements this interface is a public source of
   * {@link UpdateEvent} events.
   */
  public interface HasUpdateHandlers {

    /**
     * Adds a {@link UpdateHandler} handler for {@link UpdateEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addUpdateHandler(UpdateHandler handler);

  }

  /**
   * Handler for {@link UpdateEvent} events.
   */
  public interface UpdateHandler extends EventHandler {

    /**
     * Called after the source is updated.
     * 
     * @param event the {@link UpdateEvent} that was fired
     */
    void onUpdate(UpdateEvent event);

  }

  /**
   * Handler type.
   */
  private static Type<UpdateHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<UpdateHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<UpdateHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<UpdateHandler> getAssociatedType() {
    return (Type) TYPE;
  }
  
  @Override
  protected void dispatch(UpdateHandler handler) {
    handler.onUpdate(this);
  }

}
