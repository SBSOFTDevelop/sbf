/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.loader.FilterConfig;
import ru.sbsoft.svc.data.shared.loader.FilterConfigBean;
import ru.sbsoft.svc.widget.core.client.event.CheckChangeEvent;
import ru.sbsoft.svc.widget.core.client.menu.CheckMenuItem;

/**
 * Filters using the items in a {@link ListStore}. See {@link Filter} for more information.
 * 
 * @param <M> the model type
 * @param <V> the {@link ListStore} type
 */
public class ListFilter<M, V> extends Filter<M, V> {

  private ListMenu<M, V> listMenu;
  private ListStore<V> store;
  private boolean useStoreKeys = false;

  /**
   * Creates a list filter for the specified value provider, matching the items in the specified list store. See
   * {@link Filter#Filter(ValueProvider)} for more information.
   * 
   * @param valueProvider the value provider
   * @param store contains the items to match
   */
  public ListFilter(ValueProvider<? super M, V> valueProvider, ListStore<V> store) {
    super(valueProvider);

    this.store = store;
    listMenu = new ListMenu<M, V>(this, store);
    menu = listMenu;
  }

  @Override
  public List<FilterConfig> getFilterConfig() {
    FilterConfigBean config = new FilterConfigBean();
    config.setType("list");
    config.setValue(convertValueToString());
    return Collections.<FilterConfig> singletonList(config);
  }

  /**
   * Returns the list store.
   * 
   * @return the list store
   */
  public ListStore<V> getStore() {
    return store;
  }

  @Override
  public Object getValue() {
    List<V> values = new ArrayList<V>();
    for (V m : listMenu.getSelected()) {
      values.add(m);
    }
    return values;
  }

  @Override
  public boolean isActivatable() {
    return getValue() != null && ((List<?>) getValue()).size() > 0;
  }

  /**
   * Returns {@code true} if store keys are being used.
   * 
   * @return true to use store keys
   */
  public boolean isUseStoreKeys() {
    return useStoreKeys;
  }

  @Override
  public void setFilterConfig(List<FilterConfig> configs) {
    for (int i = 0; i < configs.size(); i++) {
      FilterConfig config = configs.get(i);

      String value = config.getValue();
      if (value != null && !"".equals(value)) {
        List<V> sel = convertValuesFromString(value);
        setValue(sel);
        setActive(true, false);
        return;
      }
    }
    setActive(false, false);
  }

  /**
   * True to serialize values using the store key rather than toString on the model value (defaults to false).
   * 
   * @param useStoreKeys true to use store keys, false to use model as a string
   */
  public void setUseStoreKeys(boolean useStoreKeys) {
    this.useStoreKeys = useStoreKeys;
  }

  /**
   * Sets the value of this filter. In order for the filter to be applied, {@link #setActive(boolean, boolean)} must be
   * called when setting filter value programmatically.
   * 
   * @param values the values
   */
  public void setValue(List<V> values) {
    listMenu.setSelected(values);
  }

  protected List<V> convertValuesFromString(String values) {
    List<V> list = new ArrayList<V>();

    String[] vals = null;
    if (values.length() > 3) {
      vals = values.split("::");
    } else {
      vals = new String[1];
      vals[0] = values;
    }

    for (int i = 0; i < vals.length; i++) {
      String value = vals[i];
      if (useStoreKeys) {
        V model = store.findModelWithKey(value);
        if (model != null) {
          list.add(model);
        }
      } else {
        for (int j = 0; j < vals.length; j++) {
          V model = store.get(j);
          String val = model.toString();
          if (value.equals(val)) {
            list.add(model);
          }
        }
      }
    }

    return list;
  }

  @SuppressWarnings("unchecked")
  protected String convertValueToString() {
    StringBuffer sb = new StringBuffer();
    List<V> temp = (List<V>) getValue();
    for (int i = 0; i < temp.size(); i++) {
      if (i != 0) sb.append("::");
      String v = useStoreKeys ? store.getKeyProvider().getKey(temp.get(i)) : temp.get(i).toString();
      sb.append(v);
    }
    return sb.toString();
  }

  @Override
  protected Class<V> getType() {
    return null;
  }

  protected void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
    setActive(isActivatable(), false);
    fireUpdate();
  }

  @Override
  protected boolean validateModel(M model) {
    Object value = getValueProvider().getValue(model);
    List<?> values = (List<?>) getValue();
    return values.size() == 0 || values.contains(value);
  }

}
