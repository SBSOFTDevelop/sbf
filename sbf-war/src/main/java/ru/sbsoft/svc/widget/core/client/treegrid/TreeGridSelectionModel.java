/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.treegrid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.GridSelectionModel;

public class TreeGridSelectionModel<M> extends GridSelectionModel<M> {

  protected TreeGrid<M> tree;
  protected TreeStore<M> treeStore;

  @Override
  public void bind(Store<M> store) {
    super.bind(store);
    if (store instanceof TreeStore<?>) {
      treeStore = (TreeStore<M>) store;
    } else {
      treeStore = null;
    }
  }

  @Override
  public void bindGrid(Grid<M> grid) {
    super.bindGrid(grid);
    if (grid instanceof TreeGrid<?>) {
      tree = (TreeGrid<M>) grid;
      treeStore = tree.getTreeStore();
    } else {
      tree = null;
    }
  }

  /**
   * Returns the currently bound tree grid.
   * 
   * @return the tree grid
   */
  public TreeGrid<M> getTreeGrid() {
    return tree;
  }

  @Override
  protected void onKeyLeft(NativeEvent ce) {
    if (Element.is(ce.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ce.getEventTarget()))) {
      return;
    }
    super.onKeyLeft(ce);
    ce.preventDefault();

    // EXTGWT-3009
    if (getLastFocused() == null) {
      return;
    }

    boolean leaf = tree.isLeaf(getLastFocused());
    if (!leaf && tree.isExpanded(getLastFocused())) {
      tree.setExpanded(getLastFocused(), false);
    } else if (!leaf) {
      M parent = treeStore.getParent(getLastFocused());
      if (parent != null) {
        select(parent, false);
      }
    } else if (leaf) {
      M parent = treeStore.getParent(getLastFocused());
      if (parent != null) {
        select(parent, false);
      }
    }
  }

  @Override
  protected void onKeyRight(NativeEvent ce) {
    if (Element.is(ce.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ce.getEventTarget()))) {
      return;
    }
    super.onKeyRight(ce);
    ce.preventDefault();
    if (!tree.isLeaf(getLastFocused()) && !tree.isExpanded(getLastFocused())) {
      tree.setExpanded(getLastFocused(), true);
    }
  }
}
