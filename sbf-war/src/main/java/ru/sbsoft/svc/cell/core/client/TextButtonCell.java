/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import com.google.gwt.core.client.GWT;

public class TextButtonCell extends ButtonCell<String> {

  public TextButtonCell() {
    this(GWT.<ButtonCellAppearance<String>> create(ButtonCellAppearance.class));
  }

  public TextButtonCell(ButtonCellAppearance<String> appearance) {
    super(appearance);
  }

}
