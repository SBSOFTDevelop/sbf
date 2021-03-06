/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import ru.sbsoft.svc.core.client.util.Format;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;

public abstract class AbstractValidator<T> implements Validator<T> {
  
  protected static List<EditorError> createError(EditorError... errors) {
    List<EditorError> list = new ArrayList<EditorError>();
    for (EditorError error : errors) {
      list.add(error);
    }
    return list;
  }
  
  protected static List<EditorError> createError(Editor<?> editor, String message, Object value) {
    return Collections.<EditorError>singletonList(new DefaultEditorError(editor, message, value));
  }
  
  protected static String encodeMessage(String message) {
    return Format.htmlEncode(message == null ? "Invalid Message" : message);
  }
}
