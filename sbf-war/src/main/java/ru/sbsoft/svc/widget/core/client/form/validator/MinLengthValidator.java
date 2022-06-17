/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.validator;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import ru.sbsoft.svc.messages.client.DefaultMessages;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;

public class MinLengthValidator extends AbstractValidator<String> {

  public interface MinLengthMessages {
    String minLengthText(int length);
  }

  protected class DefaultMinLengthMessages implements MinLengthMessages {

    @Override
    public String minLengthText(int length) {
      return DefaultMessages.getMessages().textField_minLengthText(length);
    }

  }

  protected int minLength;
  private MinLengthMessages messages;

  public MinLengthValidator(int minLength) {
    this.minLength = minLength;
  }

  public MinLengthMessages getMessages() {
    if (messages == null) {
      messages = new DefaultMinLengthMessages();
    }
    return messages;
  }

  public void setMessages(MinLengthMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<String> field, String value) {
    if (value == null) {
      return null;
    }
    int length = value.length();
    if (length < minLength) {
      return createError(new DefaultEditorError(field, getMessages().minLengthText(minLength), value));
    }
    return null;
  }

}
