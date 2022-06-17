/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.widget.core.client.event.ColumnMoveEvent.ColumnMoveHandler;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;

public final class ColumnMoveEvent extends ColumnModelEvent<ColumnMoveHandler> {
  public interface ColumnMoveHandler extends EventHandler {
    void onColumnMove(ColumnMoveEvent event);
  }

  public interface HasColumnMoveHandlers extends HasHandlers {
    HandlerRegistration addColumnMoveHandler(ColumnMoveHandler handler);
  }

  private static GwtEvent.Type<ColumnMoveHandler> TYPE;

  public static GwtEvent.Type<ColumnMoveHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<ColumnMoveHandler>();
    }
    return TYPE;
  }

  public ColumnMoveEvent(int index, ColumnConfig<?, ?> columnConfig) {
    super(index, columnConfig);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<ColumnMoveHandler> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  protected void dispatch(ColumnMoveHandler handler) {
    handler.onColumnMove(this);
  }
}
