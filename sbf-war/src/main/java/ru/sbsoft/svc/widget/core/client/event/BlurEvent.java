/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.BlurEvent.BlurHandler;

/**
 * Fires after a widget is blurred. Unlike the GWT
 * {@link com.google.gwt.event.dom.client.BlurEvent}, this event is NOT a dom
 * event to allow components flexibility in when the focus event is fired.
 */
public class BlurEvent extends GwtEvent<BlurHandler> {

  /**
   * Handler for {@link BlurEvent} events.
   */
  public interface BlurHandler extends EventHandler {

    void onBlur(BlurEvent event);

  }

  /**
   * A widget that implements this interface is a public source of
   * {@link BlurEvent} events.
   */
  public interface HasBlurHandlers {

    /**
     * Adds a {@link BlurHandler} handler for {@link BlurEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBlurHandler(BlurHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<BlurHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BlurHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BlurHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<BlurHandler> getAssociatedType() {
    return (Type) TYPE;
  }
  
  public Component getSource() {
    return (Component) super.getSource();
  }
  
  @Override
  protected void dispatch(BlurHandler handler) {
    handler.onBlur(this);
  }

}
