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
import ru.sbsoft.svc.widget.core.client.event.CellClickEvent.CellClickHandler;

public final class CellClickEvent extends GridEvent<CellClickHandler> {

  public interface HasCellClickHandlers extends HasHandlers {
    HandlerRegistration addCellClickHandler(CellClickHandler handler);
  }

  public interface CellClickHandler extends EventHandler {
    void onCellClick(CellClickEvent event);
  }

  private static GwtEvent.Type<CellClickHandler> TYPE;

  public static GwtEvent.Type<CellClickHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<CellClickHandler>();
    }
    return TYPE;
  }

  private int rowIndex;
  private int cellIndex;
  private Event event;

  public CellClickEvent(int rowIndex, int cellIndex, Event event) {
    this.rowIndex = rowIndex;
    this.cellIndex = cellIndex;
    this.event = event;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<CellClickHandler> getAssociatedType() {
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
  protected void dispatch(CellClickHandler handler) {
    handler.onCellClick(this);
  }
}