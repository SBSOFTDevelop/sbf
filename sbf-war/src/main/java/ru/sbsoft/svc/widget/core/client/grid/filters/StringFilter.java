/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid.filters;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.core.client.util.DelayedTask;
import ru.sbsoft.svc.data.shared.loader.FilterConfig;
import ru.sbsoft.svc.data.shared.loader.StringFilterHandler;
import ru.sbsoft.svc.messages.client.DefaultMessages;
import ru.sbsoft.svc.widget.core.client.event.BeforeHideEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;
import ru.sbsoft.svc.widget.core.client.form.TextField;

/**
 * A string filter. See {@link Filter} for more information.
 * 
 * @param <M> the model type
 */
public class StringFilter<M> extends Filter<M, String> {

  /**
   * The default locale-sensitive messages used by this class.
   */
  public class DefaultStringFilterMessages implements StringFilterMessages {

    @Override
    public String emptyText() {
      return DefaultMessages.getMessages().stringFilter_emptyText();
    }

  }

  /**
   * The locale-sensitive messages used by this class.
   */
  public interface StringFilterMessages {
    String emptyText();
  }

  protected TextField field;

  private StringFilterMessages messages;
  private DelayedTask updateTask = new DelayedTask() {

    @Override
    public void onExecute() {
      fireUpdate();
    }
  };

  /**
   * Creates a string filter for the specified value provider. See {@link Filter#Filter(ValueProvider)} for more
   * information.
   * 
   * @param valueProvider the value provider
   */
  public StringFilter(ValueProvider<? super M, String> valueProvider) {
    super(valueProvider);
    setHandler(new StringFilterHandler());

    field = new TextField() {
      protected void onKeyUp(Event event) {
        super.onKeyUp(event);
        onFieldKeyUp(event);
      }
    };

    menu.add(field);
    menu.addBeforeHideHandler(new BeforeHideHandler() {

      @Override
      public void onBeforeHide(BeforeHideEvent event) {
        // blur the field because of empty text
        // field.el().firstChild().blur();
        // blurField(field);
        field.getElement().selectNode("input").blur();
      }
    });

    setMessages(getMessages());
  }

  @Override
  public List<FilterConfig> getFilterConfig() {
    FilterConfig cfg = createNewFilterConfig();
    cfg.setType("string");
    cfg.setComparison("contains");
    String valueToSend = field.getCurrentValue();
    cfg.setValue(getHandler().convertToString(valueToSend));

    return Collections.singletonList(cfg);
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   * 
   * @return the local-sensitive messages used by this class.
   */
  public StringFilterMessages getMessages() {
    if (messages == null) {
      messages = new DefaultStringFilterMessages();
    }
    return messages;
  }

  @Override
  public Object getValue() {
    return field.getCurrentValue();
  }

  @Override
  public boolean isActivatable() {
    return field.getCurrentValue() != null && field.getCurrentValue().length() > 0;
  }

  /**
   * Sets the local-sensitive messages used by this class.
   * 
   * @param messages the locale sensitive messages used by this class.
   */
  public void setMessages(StringFilterMessages messages) {
    this.messages = messages;
    field.setEmptyText(messages.emptyText());
  }

  /**
   * Sets the value of this filter. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be
   * called when setting filter value programmatically.
   * 
   * @param value the value
   */
  public void setValue(String value) {
    field.setValue(value);
  }

  protected Class<String> getType() {
    return String.class;
  }

  protected void onFieldKeyUp(Event event) {
    int key = event.getKeyCode();
    if (key == KeyCodes.KEY_ENTER && field.isValid()) {
      event.stopPropagation();
      event.preventDefault();
      menu.hide(true);
      return;
    }
    updateTask.delay(getUpdateBuffer());
  }

  protected boolean validateModel(M model) {
    String val = getValueProvider().getValue(model);
    Object value = getValue();
    String v = value == null ? "" : value.toString();
    if (v.length() == 0 && (val == null || val.length() == 0)) {
      return true;
    } else if (val == null) {
      return false;
    } else {
      return val.toLowerCase().indexOf(v.toLowerCase()) > -1;
    }
  }

  @Override
  public void setFilterConfig(List<FilterConfig> configs) {
    if (configs.size() > 0) {
      String val = configs.get(0).getValue();
      setValue(val);
      setActive(val != null, false);
    } else {
      setValue(null);
      setActive(false, false);
    }
  }
}
