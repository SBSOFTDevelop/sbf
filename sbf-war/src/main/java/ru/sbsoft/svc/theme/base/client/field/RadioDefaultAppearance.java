/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import ru.sbsoft.svc.cell.core.client.form.RadioCell.RadioAppearance;

public class RadioDefaultAppearance extends CheckBoxDefaultAppearance implements RadioAppearance {

  public RadioDefaultAppearance() {
    super();
    type = "radio";
  }

  public RadioDefaultAppearance(CheckBoxResources resources) {
    super(resources);
  }

}