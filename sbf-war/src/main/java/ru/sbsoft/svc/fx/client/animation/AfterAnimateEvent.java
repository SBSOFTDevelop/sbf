/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;

/**
 * Represent the after animate event.
 */
public class AfterAnimateEvent extends GwtEvent<AfterAnimateHandler> {

  /**
   * Handler type.
   */
  private static Type<AfterAnimateHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<AfterAnimateHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<AfterAnimateHandler>();
    }
    return TYPE;
  }

  public AfterAnimateEvent() {
  }

  @Override
  public Type<AfterAnimateHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AfterAnimateHandler handler) {
    handler.onAfterAnimate(this);
  }
  
  /**
   * Handler for {@link AfterAnimateEvent} events.
   */
  public interface AfterAnimateHandler extends EventHandler {

    /**
     * Called when the animation is complete.
     * 
     * @param event the {@link AfterAnimateEvent} that was fired
     */
    void onAfterAnimate(AfterAnimateEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link AfterAnimateEvent} events.
   */
  public interface HasAfterAnimateHandlers {

    /**
     * Adds a {@link AfterAnimateHandler} handler for {@link AfterAnimateEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addAfterAnimateHandler(AfterAnimateHandler handler);

  }

}
