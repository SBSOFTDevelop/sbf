/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.FocusEvent.FocusHandler;

/**
 * Fires after a widget is focused. Unlike the GWT
 * {@link com.google.gwt.event.dom.client.FocusEvent}, this event is NOT a dom
 * event to allow components flexibility in when the focus event is fired.
 */
public class FocusEvent extends GwtEvent<FocusHandler> {

  /**
   * Handler type.
   */
  private static Type<FocusHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<FocusHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<FocusHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<FocusHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(FocusHandler handler) {
    handler.onFocus(this);
  }
  
  /**
   * Handler for {@link FocusEvent} events.
   */
  public interface FocusHandler extends EventHandler {

    void onFocus(FocusEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link FocusEvent} events.
   */
  public interface HasFocusHandlers {

    /**
     * Adds a {@link FocusHandler} handler for {@link FocusEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addFocusHandler(FocusHandler handler);
  }

}
