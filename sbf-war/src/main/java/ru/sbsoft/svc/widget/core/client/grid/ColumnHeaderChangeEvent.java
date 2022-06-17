/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.widget.core.client.event.ColumnModelEvent;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHeaderChangeEvent.ColumnHeaderChangeHandler;

public final class ColumnHeaderChangeEvent extends ColumnModelEvent<ColumnHeaderChangeHandler> {
  public interface ColumnHeaderChangeHandler extends EventHandler {
    void onColumnHeaderChange(ColumnHeaderChangeEvent event);
  }

  public interface HasColumnHeaderChangeHandlers extends HasHandlers {
    HandlerRegistration addColumnHeaderChangeHandler(ColumnHeaderChangeHandler handler);
  }

  private static GwtEvent.Type<ColumnHeaderChangeHandler> TYPE;

  public static GwtEvent.Type<ColumnHeaderChangeHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<ColumnHeaderChangeHandler>();
    }
    return TYPE;
  }

  public ColumnHeaderChangeEvent(int index, ColumnConfig<?, ?> columnConfig) {
    super(index, columnConfig);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<ColumnHeaderChangeHandler> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  protected void dispatch(ColumnHeaderChangeHandler handler) {
    handler.onColumnHeaderChange(this);
  }
}