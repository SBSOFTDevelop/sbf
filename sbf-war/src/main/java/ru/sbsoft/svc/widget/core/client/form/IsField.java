/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import ru.sbsoft.svc.widget.core.client.event.BlurEvent.HasBlurHandlers;

public interface IsField<T> extends IsWidget, LeafValueEditor<T>, HasBlurHandlers, HasValueChangeHandlers<T> {

  /**
   * Clears the value from the field.
   *
   * @see #clearInvalid() to remove validation messages
   * @see #reset() to restore to original value and remove validation messages
   */
  void clear();

  /**
   * Clear any invalid styles / messages for this field.
   */
  void clearInvalid();

  /**
   * Used to indicate that this field must be completed with its editing process, as it may be able to be removed from
   * the dom, hidden, or its current results used.
   */
  void finishEditing();

  /**
   * Returns a list of the current errors.
   * 
   * @return the errors, or empty list if no errors
   */
  List<EditorError> getErrors();

  /**
   * Returns whether or not the field value is currently valid.
   * 
   * @param preventMark {@code true} for silent validation (no invalid event and field is not marked invalid)
   * 
   * @return {@code true} if the value is valid, otherwise false
   */
  boolean isValid(boolean preventMark);

  /**
   * Resets the current field value to the originally loaded value and clears any validation messages.
   */
  void reset();

  /**
   * Validates the field value.
   * 
   * @param preventMark {@code true} to not mark the field valid and fire invalid event when invalid
   * @return {@code true} if valid, otherwise false
   */
  boolean validate(boolean preventMark);

}
