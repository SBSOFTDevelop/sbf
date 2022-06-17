/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;

/**
 * A SpinnerField that accepts integer values.
 *
 */
public class IntegerSpinnerField extends SpinnerField<Integer> {

  /**
   * Creates an IntegerSpinnerField with the default cell and appearance.
   */
  public IntegerSpinnerField() {
    super(new IntegerPropertyEditor());
  }

  /**
   * Creates an IntegerSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public IntegerSpinnerField(SpinnerFieldCell<Integer> cell) {
    super(cell);
  }

}
