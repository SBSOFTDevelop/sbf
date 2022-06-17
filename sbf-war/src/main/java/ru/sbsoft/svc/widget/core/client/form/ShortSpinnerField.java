/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.ShortPropertyEditor;

/**
 * A SpinnerField that accepts short values.
 *
 */
public class ShortSpinnerField extends SpinnerField<Short> {

  /**
   * Creates a ShortSpinnerField with the default cell and appearance.
   */
  public ShortSpinnerField() {
    super(new ShortPropertyEditor());
  }

  /**
   * Creates a ShortSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public ShortSpinnerField(SpinnerFieldCell<Short> cell) {
    super(cell);
  }

}
