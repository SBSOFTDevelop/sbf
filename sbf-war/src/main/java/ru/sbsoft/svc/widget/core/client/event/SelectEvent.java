/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Fires after a item is selected.
 */
public class SelectEvent extends GwtEvent<SelectHandler> {

  /**
   * Handler type.
   */
  private static Type<SelectHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<SelectHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<SelectHandler>();
    }
    return TYPE;
  }

  private Context context;

  public SelectEvent() {

  }

  public SelectEvent(Context context) {
    this.context = context;
  }

  /**
   * Returns the cell context.
   * 
   * @return the cell context or null if event not cell based
   */
  public Context getContext() {
    return context;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<SelectHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(SelectHandler handler) {
    handler.onSelect(this);
  }
  
  /**
   * Handler for {@link SelectEvent} events.
   */
  public interface SelectHandler extends EventHandler {

    /**
     * Called after a widget is selected.
     * 
     * @param event the {@link SelectEvent} that was fired
     */
    void onSelect(SelectEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link SelectEvent} events.
   */
  public interface HasSelectHandlers {

    /**
     * Adds a {@link SelectHandler} handler for {@link SelectEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addSelectHandler(SelectHandler handler);
  }

}
