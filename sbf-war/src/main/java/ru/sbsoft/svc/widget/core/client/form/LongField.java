/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.LongPropertyEditor;

/**
 * A Field that accepts long integer values.
 *
 */
public class LongField extends NumberField<Long> {

  /**
   * Creates an LongField with the default cell and appearance.
   */
  public LongField() {
    super(new LongPropertyEditor());
  }

  /**
   * Creates a LongField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public LongField(NumberInputCell<Long> cell) {
    super(cell, new LongPropertyEditor());
  }

}
