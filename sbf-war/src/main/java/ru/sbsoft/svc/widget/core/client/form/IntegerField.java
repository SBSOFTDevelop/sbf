/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;

/**
 * A Field that accepts integer values.
 *
 */
public class IntegerField extends NumberField<Integer> {

  /**
   * Creates an IntegerField with the default cell and appearance.
   */
  public IntegerField() {
    super(new IntegerPropertyEditor());
    setAllowDecimals(false);
  }

  /**
   * Creates an IntegerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public IntegerField(NumberInputCell<Integer> cell) {
    super(cell, new IntegerPropertyEditor());
  }
}
