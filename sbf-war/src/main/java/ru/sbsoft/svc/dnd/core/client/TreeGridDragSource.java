/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.dnd.core.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.util.Format;
import ru.sbsoft.svc.core.shared.FastSet;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.TreeStore.TreeNode;
import ru.sbsoft.svc.dnd.core.client.DND.Operation;
import ru.sbsoft.svc.dnd.core.client.DND.TreeSource;
import ru.sbsoft.svc.messages.client.DefaultMessages;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import ru.sbsoft.svc.widget.core.client.treegrid.TreeGrid;

/**
 * Enables a {@link TreeGrid} to act as the source of a drag and drop operation.
 * <p/>
 * Use {@link #setTreeGridSource(ru.sbsoft.svc.dnd.core.client.DND.TreeSource)} to specify whether leaf nodes,
 * non-leaf nodes or both types of nodes can be dragged (defaults to
 * {@link TreeSource#BOTH}). The drag operation is cancelled if the user
 * attempts to drag a node type that is not permitted.
 * <p/>
 * The drag data consists of a list of items of type {@code <M>}. It is optimized to remove children of parents that are
 * also in the list (i.e. if a parent is the subject of a drag operation then all of its children are implicitly part of
 * the drag operation).
 * 
 * @param <M> the model type
 */
public class TreeGridDragSource<M> extends DragSource {

  private TreeSource treeGridSource = TreeSource.BOTH;

  /**
   * Creates a drag source for the specified tree grid.
   * 
   * @param widget the tree grid to enable as a drag source
   */
  public TreeGridDragSource(TreeGrid<M> widget) {
    super(widget);
    setStatusText("{0} items selected");
  }

  /**
   * Returns the tree grid associated with this drag source.
   * 
   * @return the tree grid associated with this drag source
   */
  public TreeSource getTreeGridSource() {
    return treeGridSource;
  }

  @SuppressWarnings("unchecked")
  @Override
  public TreeGrid<M> getWidget() {
    return (TreeGrid<M>) super.getWidget();
  }

  /**
   * Sets the tree source (defaults to {@link TreeSource#BOTH}).
   * 
   * @param treeGridSource the tree source
   */
  public void setTreeGridSource(TreeSource treeGridSource) {
    this.treeGridSource = treeGridSource;
  }

  @Override
  protected void onDragDrop(DndDropEvent event) {
    if (event.getOperation() == Operation.MOVE) {
      @SuppressWarnings("unchecked")
      List<TreeNode<M>> sel = (List<TreeNode<M>>) event.getData();
      for (TreeNode<M> s : sel) {
        getWidget().getTreeStore().remove(s.getData());
      }
      super.data = null;
    }
  }

  @Override
  protected void onDragStart(DndDragStartEvent event) {
    Element startTarget = event.getDragStartEvent().getStartElement().<Element> cast();
    Tree.TreeNode<M> start = getWidget().findNode(startTarget);
    if (start == null || !getWidget().getTreeView().isSelectableTarget(startTarget)) {
      event.setCancelled(true);
      return;
    }

    List<M> selected = getWidget().getSelectionModel().getSelectedItems();

    if (selected.size() == 0) {
      event.setCancelled(true);
      return;
    }

    // The goal of this method is to find out which subtrees have been selected so we can
    // use them throughout this drag/drop process
    List<TreeNode<M>> selectedSubTrees = new ArrayList<TreeNode<M>>();

    if (getTreeGridSource() == TreeSource.LEAF) {
      // If only allowed to drop leaf items, check for non-leaf items
      for (M item : selected) {
        if (getWidget().isLeaf(item)) {
          selectedSubTrees.add(getWidget().getTreeStore().getSubTree(item));
        } else {
          // forget it, we've got a non-leaf
          event.setCancelled(true);
          return;
        }
      }
    } else {
      // If allowed to drop only nodes or both, then we need to remove items from the list
      // that also have their parents being dragged
      ModelKeyProvider<? super M> kp = getWidget().getTreeStore().getKeyProvider();

      // Start by looking for all selected non-leaf items
      FastSet nonLeafKeys = new FastSet();
      for (M item : selected) {
        if (getWidget().isLeaf(item)) {
          // While we're at it, if we're only allowed nodes, cancel if we see a leaf
          if (treeGridSource == TreeSource.NODE) {
            event.setCancelled(true);
            return;
          }
        } else {
          nonLeafKeys.add(kp.getKey(item));
        }
      }

      // Walking backward (so we can remove as we go) through the list of all selected items, for
      // each item, check if it has a parent already in the list.
      // Clearly that parent is a non-leaf, so will be in the set we established in the last loop
      for (int i = selected.size() - 1; i >= 0; i--) {
        // TODO consider tracking these parents, and if they are part of another
        // parent, adding them to the keyset
        M parent = selected.get(i);
        if (parent == null) { // EXTGWT-2692
          selected.remove(i);
        } else {
          while ((parent = getWidget().getTreeStore().getParent(parent)) != null) {
            // If we find that this item's parent is also selected, then we can skip this item
            if (nonLeafKeys.contains(kp.getKey(parent))) {
              selected.remove(i);
              break;
            }
          }
        }
      }
      // Finally, collect all subtrees of the items that are left
      for (M item : selected) {
        selectedSubTrees.add(getWidget().getTreeStore().getSubTree(item));
      }
    }

    if (selectedSubTrees.size() > 0) {
      event.setData(selectedSubTrees);
    } else {
      event.setCancelled(true);
    }

    if (selected.size() > 0) {
      event.setCancelled(false);

      if (getStatusText() == null) {
        event.getStatusProxy().update(SafeHtmlUtils.fromString(
            DefaultMessages.getMessages().listField_itemsSelected(selected.size())));
      } else {
        event.getStatusProxy().update(SafeHtmlUtils.fromString(Format.substitute(getStatusText(), selected.size())));
      }
    }
  }
}
