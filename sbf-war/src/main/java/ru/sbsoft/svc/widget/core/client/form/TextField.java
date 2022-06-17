/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.TextInputCell;

/**
 * A single line input field.
 */
public class TextField extends ValueBaseField<String> {

  /**
   * Creates a new text field.
   */
  public TextField() {
    this(new TextInputCell());
  }

  /**
   * Creates a new text field with the specified cell
   * 
   * @param cell a text input cell that renders the text field
   */
  public TextField(TextInputCell cell) {
    super(cell);
  }

  /**
   * Creates a new text field.
   * 
   * @param cell the input cell
   * @param propertyEditor the property editor
   */
  public TextField(TextInputCell cell, PropertyEditor<String> propertyEditor) {
    this(cell);
    setPropertyEditor(propertyEditor);
  }

}
