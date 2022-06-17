/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client.form;

import com.google.gwt.core.client.GWT;

public class RadioCell extends CheckBoxCell {

  public interface RadioAppearance extends CheckBoxAppearance {

  }

  public RadioCell() {
    this(GWT.<RadioAppearance> create(RadioAppearance.class));
  }

  public RadioCell(RadioAppearance appearance) {
    super(appearance);
  }

}
