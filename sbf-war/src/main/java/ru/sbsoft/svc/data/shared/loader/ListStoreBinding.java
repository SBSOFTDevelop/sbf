/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.List;

import ru.sbsoft.svc.data.shared.ListStore;

/**
 * Event handler for the {@link LoadEvent} fired when a {@link Loader} has
 * finished loading data. This handler takes a {@link ListStore} and uses the
 * List provided by the Loader to re-populate the store.
 * 
 * @param <C> load config object type
 * @param <M> model objects that populate the store
 * @param <D> collection passed from the loader
 */
public class ListStoreBinding<C, M, D extends List<M>> implements LoadHandler<C, D> {
  private ListStore<M> listStore;

  /**
   * Creates a {@link LoadEvent} handler for the given {@link ListStore}. It
   * expects the load result to be a list, which it uses to re-populate the
   * store.
   * 
   * @param listStore the list store
   */
  public ListStoreBinding(ListStore<M> listStore) {
    this.listStore = listStore;
  }

  @Override
  public void onLoad(LoadEvent<C, D> event) {
    List<M> data = event.getLoadResult();

    listStore.replaceAll(data);
  }

}
