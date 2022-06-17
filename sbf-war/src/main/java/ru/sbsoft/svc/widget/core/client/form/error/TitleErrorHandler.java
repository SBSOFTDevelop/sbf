/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.error;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.user.client.ui.Widget;

public class TitleErrorHandler implements ErrorHandler {

  protected Widget target;

  public TitleErrorHandler(Widget target) {
    this.target = target;
  }

  @Override
  public void clearInvalid() {
    target.setTitle("");
  }

  @Override
  public void markInvalid(List<EditorError> errors) {
    target.setTitle(errors.get(0).getMessage());
  }

  @Override
  public void release() {
    // no handlers to remove
  }
}