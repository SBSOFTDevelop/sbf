/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.math.BigInteger;

import ru.sbsoft.svc.cell.core.client.form.SpinnerFieldCell;
import ru.sbsoft.svc.widget.core.client.form.NumberPropertyEditor.BigIntegerPropertyEditor;

/**
 * A SpinnerField that accepts BigInteger values.
 *
 */
public class BigIntegerSpinnerField extends SpinnerField<BigInteger> {

  /**
   * Creates a BigIntegerSpinnerField with the default cell and appearance.
   */
  public BigIntegerSpinnerField() {
    super(new BigIntegerPropertyEditor());
  }

  /**
   * Creates a BigIntegerSpinnerField with the given cell instance. This can be used to provide an alternate
   * appearance or otherwise modify how content is rendered or events handled.
   * 
   * @param cell the cell to use to draw the field
   */
  public BigIntegerSpinnerField(SpinnerFieldCell<BigInteger> cell) {
    super(cell);
  }

}
