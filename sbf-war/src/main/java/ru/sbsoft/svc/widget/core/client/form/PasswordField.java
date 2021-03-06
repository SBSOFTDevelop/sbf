/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.PasswordInputCell;

/**
 * A single line input field where the characters are masked to prevent them
 * from being visible to others.
 */
public class PasswordField extends ValueBaseField<String> {

  /**
   * Creates a password field that allows entering a single line of text where
   * the characters are masked to prevent them from being visible to others.
   */
  public PasswordField() {
    this(new PasswordInputCell());
  }

  /**
   * Creates a new password text field.
   */
  public PasswordField(PasswordInputCell cell) {
    super(cell);
    redraw();
  }

  /**
   * Creates a new password text field.
   * 
   * @param cell the input cell
   * @param propertyEditor the property editor
   */
  public PasswordField(PasswordInputCell cell, PropertyEditor<String> propertyEditor) {
    this(cell);
    setPropertyEditor(propertyEditor);
  }

}
