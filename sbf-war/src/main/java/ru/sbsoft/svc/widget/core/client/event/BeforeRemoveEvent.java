/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.svc.widget.core.client.event.BeforeRemoveEvent.BeforeRemoveHandler;

/**
 * Fires before a widget is removed from a container.
 */
public class BeforeRemoveEvent extends GwtEvent<BeforeRemoveHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<BeforeRemoveHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeRemoveHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<BeforeRemoveHandler>();
    }
    return TYPE;
  }

  private Widget widget;
  private boolean cancelled;

  public BeforeRemoveEvent(Widget widget) {
    this.widget = widget;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Type<BeforeRemoveHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Container getSource() {
    return (Container) super.getSource();
  }

  /**
   * Returns the widget to be removed.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(BeforeRemoveHandler handler) {
    handler.onBeforeRemove(this);
  }
  
  /**
   * Handler for {@link BeforeRemoveEvent} events.
   */
  public interface BeforeRemoveHandler extends EventHandler {

    /**
     * Called before a widget is removed to a container. The action can be
     * cancelled using {@link BeforeRemoveEvent#setCancelled(boolean)}.
     * 
     * @param event the {@link BeforeRemoveEvent} that was fired
     */
    void onBeforeRemove(BeforeRemoveEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeRemoveEvent} events.
   */
  public interface HasBeforeRemoveHandlers {

    /**
     * Adds a {@link BeforeRemoveHandler} handler for
     * {@link BeforeRemoveEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeRemoveHandler(BeforeRemoveHandler handler);
  }

}
