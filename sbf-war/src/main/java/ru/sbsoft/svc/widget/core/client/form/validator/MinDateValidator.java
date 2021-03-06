/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form.validator;

import java.util.Date;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import ru.sbsoft.svc.core.client.util.DateWrapper;
import ru.sbsoft.svc.messages.client.DefaultMessages;

/**
 * Tests if the value is on the same day or later than the specified minimum date.
 */
public class MinDateValidator extends AbstractValidator<Date> {

  public interface MinDateMessages {
    String dateMinText(String max);
  }

  protected class DefaultMinDateMessages implements MinDateMessages {

    @Override
    public String dateMinText(String min) {
      return DefaultMessages.getMessages().dateField_minText(min);
    }

  }

  protected Date minDate;
  private MinDateMessages messages;
  private DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

  public MinDateValidator(Date minDate) {
    setMinDate(minDate);
  }

  public MinDateMessages getMessages() {
    if (messages == null) {
      messages = new DefaultMinDateMessages();
    }
    return messages;
  }

  public Date getMinDate() {
    return minDate;
  }

  public void setMessages(MinDateMessages messages) {
    this.messages = messages;
  }

  /**
   * Sets the minimum date. Hours, minutes, seconds, and milliseconds are cleared.
   * 
   * @param minDate the minimum date
   */
  public void setMinDate(Date minDate) {
    this.minDate = new DateWrapper(minDate).clearTime().asDate();
  }

  @Override
  public List<EditorError> validate(Editor<Date> field, Date value) {
    if (value != null && value.before(minDate)) {
      return createError(field, getMessages().dateMinText(format.format(minDate)), value);
    }
    return null;
  }
}
