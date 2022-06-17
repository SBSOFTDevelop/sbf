/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.error;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import ru.sbsoft.svc.widget.core.client.Component;

public class ToolTipErrorHandler implements ErrorHandler {

  protected Component target;
  
  public ToolTipErrorHandler(Component target) {
    this.target = target;
  }

  @Override
  public void clearInvalid() {
    target.hideToolTip();
    if (target.getToolTip() != null) {
      target.getToolTip().disable();
    }
  }

  @Override
  public void markInvalid(List<EditorError> errors) {
    target.setToolTip(errors.get(0).getMessage());
    target.getToolTip().addStyleName("x-form-invalid-tip");
    target.getToolTip().enable();
  }

  @Override
  public void release() {
    //no handlers to remove
  }
}