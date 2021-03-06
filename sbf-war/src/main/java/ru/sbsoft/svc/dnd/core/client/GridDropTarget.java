/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.dnd.core.client;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Point;
import ru.sbsoft.svc.core.client.util.Rectangle;
import ru.sbsoft.svc.dnd.core.client.DND.Feedback;
import ru.sbsoft.svc.dnd.core.client.DND.Operation;
import ru.sbsoft.svc.widget.core.client.event.XEvent;
import ru.sbsoft.svc.widget.core.client.grid.Grid;

/**
 * Enables a {@link Grid} to act as the target of a drag and drop operation.
 * <p/>
 * Use {@link #setFeedback(ru.sbsoft.svc.dnd.core.client.DND.Feedback)} to specify whether to allow inserting
 * items between rows, appending items to the end, or both (defaults to
 * {@link Feedback#BOTH}).
 * <p/>
 * Use {@link #setOperation(ru.sbsoft.svc.dnd.core.client.DND.Operation)} to specify whether to move items or copy
 * them (defaults to {@link Operation#MOVE}).
 *
 * @param <M> the model type
 */
public class GridDropTarget<M> extends DropTarget {

  protected M activeItem;
  protected Grid<M> grid;
  protected int insertIndex;
  boolean before;

  /**
   * Creates a drop target for the specified grid.
   * 
   * @param grid the grid to enable as a drop target
   */
  public GridDropTarget(Grid<M> grid) {
    super(grid);
    this.grid = grid;
  }

  /**
   * Returns the grid associated with this drop target.
   * 
   * @return the grid associated with this drop target
   */
  public Grid<M> getGrid() {
    return grid;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void onDragDrop(DndDropEvent e) {
    super.onDragDrop(e);
    Object data = e.getData();
    List<M> models = (List<M>) prepareDropData(data, true);
    if (models.size() > 0) {
      if (feedback == Feedback.APPEND) {
        grid.getStore().addAll(models);
      } else {
        grid.getStore().addAll(insertIndex, models);
      }
    }
    insertIndex = -1;
    activeItem = null;
  }

  @Override
  protected void onDragEnter(DndDragEnterEvent e) {
    super.onDragEnter(e);
    e.setCancelled(false);
    e.getStatusProxy().setStatus(true);
  }

  @Override
  protected void onDragMove(DndDragMoveEvent event) {
    Element target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    if (Element.is(target) && !grid.getView().getScroller().isOrHasChild(Element.as(target))) {
      event.setCancelled(true);
      event.getStatusProxy().setStatus(false);
      return;
    }

    event.setCancelled(false);
    event.getStatusProxy().setStatus(true);
  }

  @Override
  protected void showFeedback(DndDragMoveEvent event) {
    event.getStatusProxy().setStatus(true);
    Element target = getElementFromEvent(event.getDragMoveEvent().getNativeEvent());

    if (feedback == Feedback.INSERT || feedback == Feedback.BOTH) {
      Element row = grid.getView().findRow(Element.as(target)).cast();

      if (row == null && grid.getStore().size() > 0) {
        row = grid.getView().getRow(grid.getStore().size() - 1).cast();
      }

      if (row != null) {
        int height = row.getOffsetHeight();
        int mid = height / 2;
        mid += row.getAbsoluteTop();
        int y = event.getDragMoveEvent().getNativeEvent().<XEvent>cast().getXY().getY();
        before = y < mid;
        int idx = grid.getView().findRowIndex(row);

        activeItem = grid.getStore().get(idx);
        insertIndex = adjustIndex(event, idx);

        showInsert(event, row);
      } else {
        insertIndex = 0;
      }
    }
  }

  private int adjustIndex(DndDragMoveEvent event, int index) {
    Object data = event.getData();
    int i = index;
    @SuppressWarnings("unchecked")
    List<M> models = (List<M>) prepareDropData(data, true);
    for (M m : models) {
      int idx = grid.getStore().indexOf(m);
      if (idx > -1 && (before ? idx < index : idx <= index)) {
        i--;
      }
    }
    return before ? i : i + 1;
  }

  private void showInsert(DndDragMoveEvent event, Element row) {
    Insert insert = Insert.get();
    insert.show(row);
    Rectangle rect = XElement.as(row).getBounds();
    int y = !before ? (rect.getY() + rect.getHeight() - 4) : rect.getY() - 2;
    insert.getElement().setBounds(rect.getX(), y, rect.getWidth(), 6);
  }

}
