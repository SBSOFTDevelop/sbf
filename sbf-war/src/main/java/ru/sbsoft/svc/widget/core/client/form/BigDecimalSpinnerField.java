/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.math.BigDecimal;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.BigDecimalPropertyEditor;

/**
 * A SpinnerField that accepts BigDecimal values.
 *
 */
public class BigDecimalSpinnerField extends SpinnerField<BigDecimal> {

  /**
   * Creates a BigDecimalSpinnerField with the default cell and appearance.
   */
  public BigDecimalSpinnerField() {
    super(new BigDecimalPropertyEditor());
  }

  /**
   * Creates a BigDecimalSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public BigDecimalSpinnerField(SpinnerFieldCell<BigDecimal> cell) {
    super(cell);
  }

}
