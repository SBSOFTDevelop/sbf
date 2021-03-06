/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client.form;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class PasswordInputCell extends TextInputCell {

  @Override
  public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
    ViewData viewData = checkViewData(context, value);
    String s = (viewData != null) ? viewData.getCurrentValue() : value;

    FieldAppearanceOptions options = new FieldAppearanceOptions(getWidth(), getHeight(), isReadOnly(), getEmptyText());
    options.setName(name);
    options.setEmptyText(getEmptyText());
    options.setDisabled(isDisabled());
    getAppearance().render(sb, "password", s == null ? "" : s, options);
  }
}
