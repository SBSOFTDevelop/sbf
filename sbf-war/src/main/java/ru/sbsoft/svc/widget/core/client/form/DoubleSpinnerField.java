/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
/**
 * A SpinnerField that accepts double values.
 *
 */
public class DoubleSpinnerField extends SpinnerField<Double> {

  /**
   * Creates a DoubleSpinnerField with the default cell and appearance.
   */
  public DoubleSpinnerField() {
    super(new DoublePropertyEditor());
  }

  /**
   * Creates a DoubleSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public DoubleSpinnerField(SpinnerFieldCell<Double> cell) {
    super(cell);
  }

}
