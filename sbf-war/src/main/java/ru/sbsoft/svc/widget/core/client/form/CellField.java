/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import com.google.gwt.dom.client.Element;
import com.google.gwt.view.client.ProvidesKey;
import ru.sbsoft.svc.cell.core.client.form.FieldCell;
import ru.sbsoft.svc.widget.core.client.cell.CellComponent;

public class CellField<C> extends CellComponent<C> {

  public CellField(FieldCell<C> cell) {
    super(cell);
  }

  public CellField(FieldCell<C> cell, C initialValue) {
    super(cell, initialValue);
  }

  public CellField(FieldCell<C> cell, C initialValue, ProvidesKey<C> keyProvider) {
    super(cell, initialValue, keyProvider);
  }

  public CellField(FieldCell<C> cell, C initialValue, ProvidesKey<C> keyProvider, Element elem) {
    super(cell, initialValue, keyProvider, elem);
  }

  public CellField(FieldCell<C> cell, ProvidesKey<C> keyProvider) {
    super(cell, keyProvider);
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    redraw();
  }

}
