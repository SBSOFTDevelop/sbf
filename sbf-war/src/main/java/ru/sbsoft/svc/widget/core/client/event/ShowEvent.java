/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.ShowEvent.ShowHandler;

/**
 * Fires after a widget is shown.
 */
public class ShowEvent extends GwtEvent<ShowHandler> {

  /**
   * A widget that implements this interface is a public source of
   * {@link ShowEvent} events.
   */
  public interface HasShowHandlers {

    /**
     * Adds a {@link ShowHandler} handler for {@link ShowEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addShowHandler(ShowHandler handler);

  }

  /**
   * Handler for {@link ShowEvent} events.
   */
  public interface ShowHandler extends EventHandler {

    /**
     * Called after a widget is shown.
     * 
     * @param event the {@link ShowEvent} that was fired
     */
    void onShow(ShowEvent event);

  }

  /**
   * Handler type.
   */
  private static Type<ShowHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ShowHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<ShowHandler>());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Type<ShowHandler> getAssociatedType() {
    return (Type) TYPE;
  }
  
  @Override
  public Component getSource() {
    return (Component) super.getSource();
  }
  
  @Override
  protected void dispatch(ShowHandler handler) {
    handler.onShow(this);
  }

}
