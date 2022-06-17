/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.FloatPropertyEditor;

/**
 * A SpinnerField that accepts float values.
 *
 */
public class FloatSpinnerField extends SpinnerField<Float> {

  /**
   * Creates a FloatSpinnerField with the default cell and appearance.
   */
  public FloatSpinnerField() {
    super(new FloatPropertyEditor());
  }

  /**
   * Creates a FloatSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public FloatSpinnerField(SpinnerFieldCell<Float> cell) {
    super(cell);
  }

}
