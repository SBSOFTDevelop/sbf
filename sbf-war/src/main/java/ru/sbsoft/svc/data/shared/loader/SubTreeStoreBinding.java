/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.List;

import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.data.shared.TreeStore.TreeNode;

/**
 * Event handler for the {@link LoadEvent} fired when a {@link Loader} has
 * finished loading data. It expects {@link LoadEvent#getLoadConfig} to return a
 * parent and {@link LoadEvent#getLoadResult} to return a list of new children.
 * It replaces the children of the given parent with the list of new children.
 * The children are expected to be of type {@link TreeNode}. For children of
 * type {@code <M>} see {@link ChildTreeStoreBinding}.
 * 
 * @param <M> the type of objects that populate the store
 * @param <T> the type of <code>TreeNode</code> for the store
 */
public class SubTreeStoreBinding<M, T extends TreeStore.TreeNode<M>> implements LoadHandler<M, List<T>> {
  private final TreeStore<M> store;

  /**
   * Creates a {@link LoadEvent} handler for the given {@link TreeStore}.
   * 
   * @param store the store whose events will be handled
   */
  public SubTreeStoreBinding(TreeStore<M> store) {
    this.store = store;
  }

  @Override
  public void onLoad(LoadEvent<M, List<T>> event) {
    M parent = event.getLoadConfig();

    store.replaceSubTree(parent, event.getLoadResult());
  }
}
