/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.MoveEvent.MoveHandler;

/**
 * Fires after a widget is moved.
 */
public class MoveEvent extends GwtEvent<MoveHandler> {

  /**
   * Handler type.
   */
  private static Type<MoveHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<MoveHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<MoveHandler>());
  }

  private int x;
  private int y;

  public MoveEvent(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<MoveHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  /**
   * Returns the widget's page coordinates.
   * 
   * @return the x-coordinate value
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the widget's page coordinates.
   * 
   * @return the y-coordinate value
   */
  public int getY() {
    return y;
  }

  @Override
  protected void dispatch(MoveHandler handler) {
    handler.onMove(this);
  }
  
  /**
   * Handler class for {@link MoveEvent} events.
   */
  public interface MoveHandler extends EventHandler {

    /**
     * Called after the widget is moved.
     */
    void onMove(MoveEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link MoveEvent} events.
   */
  public interface HasMoveHandlers {

    /**
     * Adds a {@link MoveHandler} handler for {@link MoveEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addMoveHandler(MoveHandler handler);

  }

}
