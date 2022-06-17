/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.ShortPropertyEditor;

/**
 * A Field that accepts short integer values.
 *
 */
public class ShortField extends NumberField<Short> {

  /**
   * Creates a ShortField with the default cell and appearance.
   */
  public ShortField() {
    super(new ShortPropertyEditor());
  }

  /**
   * Creates an ShortField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public ShortField(NumberInputCell<Short> cell) {
    super(cell, new ShortPropertyEditor());
  }

}
