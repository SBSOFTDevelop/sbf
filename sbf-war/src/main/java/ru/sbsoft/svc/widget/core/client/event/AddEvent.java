/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.svc.widget.core.client.event.AddEvent.AddHandler;

/**
 * Fires after a widget is added to a container.
 */
public class AddEvent extends GwtEvent<AddHandler> {

  /**
   * Handler type.
   */
  private static Type<AddHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<AddHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<AddHandler>();
    }
    return TYPE;
  }

  private Widget widget;
  private int index;

  public AddEvent(Widget widget, int index) {
    this.widget = widget;
    this.index = index;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Type<AddHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the insert index of the widget that was added.
   * 
   * @return the insert index
   */
  public int getIndex() {
    return index;
  }

  public Container getSource() {
    return (Container) super.getSource();
  }

  /**
   * Returns the widget that was added.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  protected void dispatch(AddHandler handler) {
    handler.onAdd(this);
  }
  
  /**
   * Handler for {@link AddEvent} events.
   */
  public interface AddHandler extends EventHandler {

    /**
     * Called after a widget is added to a container.
     * 
     * @param event the {@link AddEvent} that was fired
     */
    void onAdd(AddEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link AddEvent} events.
   */
  public interface HasAddHandlers {

    /**
     * Adds a {@link AddHandler} handler for {@link AddEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addAddHandler(AddHandler handler);
  }

}
