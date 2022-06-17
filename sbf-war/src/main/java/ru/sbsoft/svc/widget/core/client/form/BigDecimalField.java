/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.math.BigDecimal;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.BigDecimalPropertyEditor;

/**
 * A Field that accepts BigDecimal values.
 *
 */
public class BigDecimalField extends NumberField<BigDecimal> {

  /**
   * Creates a BigDecimalField with the default cell and appearance.
   */
  public BigDecimalField() {
    super(new BigDecimalPropertyEditor());
  }

  /**
   * Creates a BigDecimalField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public BigDecimalField(NumberInputCell<BigDecimal> cell) {
    super(cell, new BigDecimalPropertyEditor());
  }
}
