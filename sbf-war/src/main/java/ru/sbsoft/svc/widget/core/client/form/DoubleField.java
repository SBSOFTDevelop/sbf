/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;

/**
 * A Field that accepts double values.
 */
public class DoubleField extends NumberField<Double> {

  /**
   * Creates a DoubleField with the default cell and appearance.
   */
  public DoubleField() {
    super(new DoublePropertyEditor());
  }

  /**
   * Creates a DoubleField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   */
  public DoubleField(NumberInputCell<Double> cell) {
    super(cell, new DoublePropertyEditor());
  }

}
