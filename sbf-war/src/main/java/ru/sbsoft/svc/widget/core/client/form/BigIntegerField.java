/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.math.BigInteger;

import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.BigIntegerPropertyEditor;

/**
 * A Field that accepts BigInteger values.
 *
 */
public class BigIntegerField extends NumberField<BigInteger> {

  /**
   * Creates a BigIntegerField with the default cell and appearance.
   */
  public BigIntegerField() {
    super(new BigIntegerPropertyEditor());
  }

  /**
   * Creates a BigIntegerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public BigIntegerField(NumberInputCell<BigInteger> cell) {
    super(cell, new BigIntegerPropertyEditor());
  }
}
