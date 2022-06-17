/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.widget.core.client.event.CellMouseDownEvent.CellMouseDownHandler;

public final class CellMouseDownEvent extends GridEvent<CellMouseDownHandler> {

  public interface HasCellMouseDownHandlers extends HasHandlers {
    HandlerRegistration addCellMouseDownHandler(CellMouseDownHandler handler);
  }

  public interface CellMouseDownHandler extends EventHandler {
    void onCellMouseDown(CellMouseDownEvent event);
  }

  private static GwtEvent.Type<CellMouseDownHandler> TYPE;

  public static GwtEvent.Type<CellMouseDownHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<CellMouseDownHandler>();
    }
    return TYPE;
  }

  private int rowIndex;
  private int cellIndex;
  private Event event;

  public CellMouseDownEvent(int rowIndex, int cellIndex, Event event) {
    this.rowIndex = rowIndex;
    this.cellIndex = cellIndex;
    this.event = event;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<CellMouseDownHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }

  public int getCellIndex() {
    return cellIndex;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public Event getEvent() {
    return event;
  }

  @Override
  protected void dispatch(CellMouseDownHandler handler) {
    handler.onCellMouseDown(this);
  }
}