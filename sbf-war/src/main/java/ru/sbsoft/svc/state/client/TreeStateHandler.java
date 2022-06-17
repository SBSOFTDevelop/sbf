/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import java.util.Set;

import ru.sbsoft.svc.core.shared.FastSet;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import ru.sbsoft.svc.state.client.TreeStateHandler.TreeState;
import ru.sbsoft.svc.widget.core.client.event.CollapseItemEvent;
import ru.sbsoft.svc.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import ru.sbsoft.svc.widget.core.client.event.ExpandItemEvent;
import ru.sbsoft.svc.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import ru.sbsoft.svc.widget.core.client.tree.Tree;

/**
 * Watches the expanded state of the tree, and stores it under the given key.
 * 
 * @param <M> the data model type
 */
public class TreeStateHandler<M> extends ComponentStateHandler<TreeState, Tree<M, ?>> {
  public interface TreeState {
    Set<String> getExpandedKeys();

    void setExpandedKeys(Set<String> keys);
  }

  class Handler implements CollapseItemHandler<M>, ExpandItemHandler<M>, StoreDataChangeHandler<M> {
    @Override
    public void onCollapse(CollapseItemEvent<M> event) {
      TreeStateHandler.this.onCollapse(event.getItem());
    }

    @Override
    public void onDataChange(StoreDataChangeEvent<M> event) {
      applyState();
    }

    @Override
    public void onExpand(ExpandItemEvent<M> event) {
      TreeStateHandler.this.onExpand(event.getItem());
      applyState();
    }
  }

  public TreeStateHandler(Tree<M, ?> component) {
    super(TreeState.class, component);

    Handler h = new Handler();
    component.addCollapseHandler(h);
    component.addExpandHandler(h);
    component.getStore().addStoreDataChangeHandler(h);
  }

  public TreeStateHandler(Tree<M, ?> component, String key) {
    super(TreeState.class, component, key);

    Handler h = new Handler();
    component.addCollapseHandler(h);
    component.addExpandHandler(h);
    component.getStore().addStoreDataChangeHandler(h);
  }

  @Override
  public void applyState() {
    Store<M> store = getObject().getStore();

    // TODO refactor this part of the method (and its callers) to iterate over
    // the elements just made visible, and only consider expanding them
    for (String key : getSet(getState())) {
      M item = store.findModelWithKey(key);
      if (item != null) {
        getObject().setExpanded(item, true);
      }
    }
  }

  protected Set<String> getSet(TreeState state) {
    if (state.getExpandedKeys() == null) {
      state.setExpandedKeys(new FastSet());
    }
    return state.getExpandedKeys();
  }

  protected void onCollapse(M item) {
    String key = getObject().getStore().getKeyProvider().getKey(item);
    getSet(getState()).remove(key);

    saveState();
  }

  protected void onExpand(M item) {
    String key = getObject().getStore().getKeyProvider().getKey(item);
    getSet(getState()).add(key);

    saveState();
  }

}
