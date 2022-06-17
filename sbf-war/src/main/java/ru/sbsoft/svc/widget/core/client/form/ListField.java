/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.core.client.Style.SelectionMode;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.ListView;

/**
 * Adapts a {@link ListView} for use as a field which can have a single value selected. For multi-select,
 * consider {@link DualListField}.
 * 
 * @param <M> the model type
 * @param <T> the field's data type
 */
public class ListField<M, T> extends AdapterField<M> {

  protected ListView<M, T> listView;

  /**
   * Creates a list field that wraps a {@link ListView} for use as a field.
   * 
   * @param view the list view to wrap
   */
  public ListField(ListView<M, T> view) {
    super(view);
    this.listView = view;
    this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  /**
   * Returns the store used by the list view.
   * 
   * @return the store used by the list view
   */
  public ListStore<M> getStore() {
    return listView.getStore();
  }

  @Override
  public M getValue() {
    return listView.getSelectionModel().getSelectedItem();
  }

  /**
   * Sets the list field's list store.
   * 
   * @param store the store
   */
  public void setStore(ListStore<M> store) {
    listView.setStore(store);
  }

  @Override
  public void setValue(M value) {
    if (getStore().indexOf(value) == -1) {
      listView.getSelectionModel().deselectAll();
    } else {
      listView.getSelectionModel().select(value, false);
    }
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    listView.disable();
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    listView.enable();
  }
}
