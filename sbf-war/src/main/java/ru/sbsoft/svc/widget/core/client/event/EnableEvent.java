/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.EnableEvent.EnableHandler;

/**
 * Fires after a widget is enabled.
 */
public class EnableEvent extends GwtEvent<EnableHandler> {

  /**
   * Handler type.
   */
  private static Type<EnableHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<EnableHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<EnableHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<EnableHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(EnableHandler handler) {
    handler.onEnable(this);
  }
  
  /**
   * Handler for {@link EnableEvent} events.
   */
  public interface EnableHandler extends EventHandler {

    void onEnable(EnableEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link EnableEvent} events.
   * 
   */
  public interface HasEnableHandlers {

    /**
     * Adds a {@link EnableHandler} handler for {@link EnableEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addEnableHandler(EnableHandler handler);

  }

}
