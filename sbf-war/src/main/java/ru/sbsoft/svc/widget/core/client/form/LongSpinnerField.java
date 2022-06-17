/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.LongPropertyEditor;

/**
 * A SpinnerField that accepts long values.
 *
 */
public class LongSpinnerField extends SpinnerField<Long> {

  /**
   * Creates a LongSpinnerField with the default cell and appearance.
   */
  public LongSpinnerField() {
    super(new LongPropertyEditor());
  }

  /**
   * Creates a LongSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public LongSpinnerField(SpinnerFieldCell<Long> cell) {
    super(cell);
  }

}