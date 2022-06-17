/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.error;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import ru.sbsoft.svc.core.client.util.Format;

public class DefaultEditorError implements EditorError {

  protected Editor<?> editor;
  protected String message;
  protected boolean consumed;
  protected Object value;
  
  public DefaultEditorError(Editor<?> editor, String message, Object value) {
    this.editor = editor;
    this.message = message;
    this.value = value;
  }
  
  @Override
  public boolean isConsumed() {
    return consumed;
  }

  @Override
  public String getAbsolutePath() {
    return null;
  }

  @Override
  public Editor<?> getEditor() {
    return editor;
  }

  @Override
  public String getMessage() {
    return Format.substitute(message, value);
  }

  @Override
  public String getPath() {
    return null;
  }

  @Override
  public Object getUserData() {
    return null;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setConsumed(boolean consumed) {
    this.consumed = consumed;
  }
}
