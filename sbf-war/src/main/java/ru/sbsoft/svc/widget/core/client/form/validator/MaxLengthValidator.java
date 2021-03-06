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

public class MaxLengthValidator extends AbstractValidator<String> {

  public interface MaxLengthMessages {
    String maxLengthText(int length);
  }

  protected class DefaultMaxLengthMessages implements MaxLengthMessages {

    @Override
    public String maxLengthText(int length) {
      return DefaultMessages.getMessages().textField_maxLengthText(length);
    }

  }

  protected int maxLength;
  private MaxLengthMessages messages;

  public MaxLengthValidator(int maxLength) {
    this.maxLength = maxLength;
  }

  public MaxLengthMessages getMessages() {
    if (messages == null) {
      messages = new DefaultMaxLengthMessages();
    }
    return messages;
  }

  public void setMessages(MaxLengthMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<String> field, String value) {
    if (value == null) return null;
    int length = value.length();
    if (length > maxLength) {
      return createError(new DefaultEditorError(field, getMessages().maxLengthText(maxLength), value));
    }
    return null;
  }

}
