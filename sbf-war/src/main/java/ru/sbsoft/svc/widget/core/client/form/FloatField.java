/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.FloatPropertyEditor;

/**
 * A Field that accepts float values.
 *
 */
public class FloatField extends NumberField<Float> {

  /**
   * Creates a FloatField with the default cell and appearance.
   */
  public FloatField() {
    super(new FloatPropertyEditor());
  }

  /**
   * Creates a FloatField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public FloatField(NumberInputCell<Float> cell) {
    super(cell, new FloatPropertyEditor());
  }

}
