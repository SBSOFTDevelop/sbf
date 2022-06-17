/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid.filters;

import java.util.Collections;
import java.util.List;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.data.shared.loader.BooleanFilterHandler;
import ru.sbsoft.svc.data.shared.loader.FilterConfig;
import ru.sbsoft.svc.data.shared.loader.FilterConfigBean;
import ru.sbsoft.svc.messages.client.DefaultMessages;
import ru.sbsoft.svc.widget.core.client.event.CheckChangeEvent;
import ru.sbsoft.svc.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import ru.sbsoft.svc.widget.core.client.menu.CheckMenuItem;

/**
 * A boolean filter. See {@link Filter} for more information.
 * 
 * @param <M> the model type
 */
public class BooleanFilter<M> extends Filter<M, Boolean> {

  /**
   * The locale-sensitive messages used by this class.
   */
  public interface BooleanFilterMessages {
    String noText();

    String yesText();
  }

  /**
   * The default locale-sensitive messages used by this class.
   */
  public class DefaultBooleanFilterMessages implements BooleanFilterMessages {

    @Override
    public String noText() {
      return DefaultMessages.getMessages().booleanFilter_noText();
    }

    @Override
    public String yesText() {
      return DefaultMessages.getMessages().booleanFilter_yesText();
    }

  }

  private CheckMenuItem yesItem, noItem;
  private BooleanFilterMessages messages = new DefaultBooleanFilterMessages();
  private CheckChangeHandler<CheckMenuItem> handler = new CheckChangeHandler<CheckMenuItem>() {

    @Override
    public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
      fireUpdate();
    }
  };

  /**
   * Creates a boolean filter for the specified value provider. See
   * {@link Filter#Filter(ValueProvider)} for more information.
   * 
   * @param valueProvider the value provider
   */
  public BooleanFilter(ValueProvider<? super M, Boolean> valueProvider) {
    super(valueProvider);

    setHandler(new BooleanFilterHandler());

    yesItem = new CheckMenuItem();
    yesItem.addCheckChangeHandler(handler);
    yesItem.setGroup(XDOM.getUniqueId());

    noItem = new CheckMenuItem();
    noItem.addCheckChangeHandler(handler);
    noItem.setGroup(yesItem.getGroup());

    menu.add(yesItem);
    menu.add(noItem);

    setMessages(messages);
  }

  @Override
  public List<FilterConfig> getFilterConfig() {
    FilterConfigBean config = new FilterConfigBean();
    config.setType("boolean");
    config.setValue(getHandler().convertToString((Boolean) getValue()));
    return Collections.<FilterConfig>singletonList(config);
  }

  /**
   * Returns the locale-sensitive messages used by this class.
   * 
   * @return the local-sensitive messages used by this class.
   */
  public BooleanFilterMessages getMessages() {
    return messages;
  }

  @Override
  public Object getValue() {
    return Boolean.valueOf(yesItem.isChecked());
  }

  @Override
  public boolean isActive() {
    return super.isActive();
  }

  @Override
  public void setFilterConfig(List<FilterConfig> configs) {
    if (configs.size() == 0) {
      setActive(false, false);
    } else {
      FilterConfig config = configs.get(0);
      setValue(Boolean.parseBoolean(config.getValue()));
      setActive(true, false);
    }
  }

  /**
   * Sets the local-sensitive messages used by this class.
   * 
   * @param messages the locale sensitive messages used by this class.
   */
  public void setMessages(BooleanFilterMessages messages) {
    this.messages = messages;
    yesItem.setText(getMessages().yesText());
    noItem.setText(getMessages().noText());
  }

  /**
   * Sets the value of this filter. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be
   * called when setting filter value programmatically.
   * 
   * @param value the value
   */
  public void setValue(boolean value) {
    noItem.setChecked(!value);
    yesItem.setChecked(value);
  }

  @Override
  protected Class<Boolean> getType() {
    return Boolean.class;
  }

  @Override
  protected boolean isActivatable() {
    return super.isActivatable();
  }

  @Override
  protected boolean validateModel(M model) {
    Boolean val = getValueProvider().getValue(model);
    return getValue().equals(val == null ? Boolean.FALSE : val);
  }

}
