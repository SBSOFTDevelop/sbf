/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;

public abstract class ColumnModelEvent<H extends EventHandler> extends GwtEvent<H> {
  private final ColumnConfig<?, ?> columnConfig;

  private final int index;

  public ColumnModelEvent(int index, ColumnConfig<?, ?> columnConfig) {
    this.index = index;
    this.columnConfig = columnConfig;
  }

  public ColumnConfig<?, ?> getColumnConfig() {
    return (ColumnConfig<?, ?>) columnConfig;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public ColumnModel<?> getSource() {
    return (ColumnModel<?>) super.getSource();
  }
}