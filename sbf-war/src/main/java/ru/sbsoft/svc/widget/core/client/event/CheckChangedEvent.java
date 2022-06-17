/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;

/**
 * Fires after an objects check state is changed.
 */
public class CheckChangedEvent<T> extends GwtEvent<CheckChangedHandler<T>> {

  /**
   * Handler type.
   */
  private static Type<CheckChangedHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CheckChangedHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<CheckChangedHandler<?>>();
    }
    return TYPE;
  }

  private List<T> checked;

  public CheckChangedEvent(List<T> checked) {
    this.checked = Collections.unmodifiableList(checked);
  }

  public List<T> getItems() {
    return checked;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<CheckChangedHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(CheckChangedHandler<T> handler) {
    handler.onCheckChanged(this);
  }
  
  /**
   * Handler class for {@link CheckChangeEvent} events.
   */
  public interface CheckChangedHandler<T> extends EventHandler {

    /**
     * Called when the checked selection changes.
     */
    void onCheckChanged(CheckChangedEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link CheckChangedEvent} events.
   */
  public interface HasCheckChangedHandlers<T> {

    /**
     * Adds a {@link CheckChangedHandler} handler for {@link CheckChangedEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addCheckChangedHandler(CheckChangedHandler<T> handler);
  }

}
