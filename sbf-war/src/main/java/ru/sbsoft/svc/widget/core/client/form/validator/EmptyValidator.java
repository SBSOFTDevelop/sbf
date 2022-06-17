/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.validator;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import ru.sbsoft.svc.messages.client.DefaultMessages;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;

public class EmptyValidator<T> implements Validator<T> {

  protected static class DefaultEmptyMessages implements EmptyMessages {

    @Override
    public String blankText() {
      return DefaultMessages.getMessages().textField_blankText();
    }

  }

  public interface EmptyMessages {
    String blankText();
  }

  private EmptyMessages messages;

  public EmptyMessages getMessages() {
    if (messages == null) {
      messages = new DefaultEmptyMessages();
    }
    return messages;
  }

  public void setMessages(EmptyMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<T> editor, T value) {
    if (value == null || "".equals(value)) {
      List<EditorError> errors = new ArrayList<EditorError>();
      errors.add(new DefaultEditorError(editor, getMessages().blankText(), ""));
      return errors;
    }
    return null;
  }

}
