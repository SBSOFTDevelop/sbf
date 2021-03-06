/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import java.util.Collections;
import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.InvalidEvent.InvalidHandler;
import ru.sbsoft.svc.widget.core.client.form.Field;

/**
 * Fires after a field value marked invalid.
 */
public class InvalidEvent extends GwtEvent<InvalidHandler> {

  /**
   * Handler type.
   */
  private static Type<InvalidHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<InvalidHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<InvalidHandler>());
  }

  private List<EditorError> errors;

  public InvalidEvent(List<EditorError> errors) {
    this.errors = Collections.unmodifiableList(errors);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<InvalidHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public List<EditorError> getErrors() {
    return errors;
  }

  @Override
  public Field<?> getSource() {
    return (Field<?>) super.getSource();
  }

  @Override
  protected void dispatch(InvalidHandler handler) {
    handler.onInvalid(this);
  }
  
  /**
   * Handler class for {@link InvalidEvent} events.
   */
  public interface InvalidHandler extends EventHandler {

    /**
     * Called when a field becomes valid.
     */
    void onInvalid(InvalidEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link InvalidEvent} events.
   */
  public interface HasInvalidHandlers {

    /**
     * Adds a {@link InvalidHandler} handler for {@link InvalidEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addInvalidHandler(InvalidHandler handler);

  }

}
