/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import ru.sbsoft.svc.widget.core.client.grid.Grid.GridCell;
import ru.sbsoft.svc.widget.core.client.grid.editing.GridEditing;

public abstract class GridEditingEvent<M, H extends EventHandler> extends GwtEvent<H> {
  private final GridCell editCell;

  public GridEditingEvent(GridCell editCell) {
    this.editCell = editCell;
  }

  public GridCell getEditCell() {
    return editCell;
  }

  @SuppressWarnings("unchecked")
  @Override
  public GridEditing<M> getSource() {
    return (GridEditing<M>) super.getSource();
  }
}