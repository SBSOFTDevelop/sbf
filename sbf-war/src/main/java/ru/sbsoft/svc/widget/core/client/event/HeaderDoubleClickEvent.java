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
import ru.sbsoft.svc.widget.core.client.event.HeaderDoubleClickEvent.HeaderDoubleClickHandler;

public final class HeaderDoubleClickEvent extends GridEvent<HeaderDoubleClickHandler> {

  public interface HasHeaderDoubleClickHandlers extends HasHandlers {
    HandlerRegistration addHeaderDoubleClickHandler(HeaderDoubleClickHandler handler);
  }

  public interface HeaderDoubleClickHandler extends EventHandler {
    void onHeaderDoubleClick(HeaderDoubleClickEvent event);
  }

  private static GwtEvent.Type<HeaderDoubleClickHandler> TYPE;

  public static GwtEvent.Type<HeaderDoubleClickHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<HeaderDoubleClickHandler>();
    }
    return TYPE;
  }

  private int columnIndex;
  private Event event;

  public HeaderDoubleClickEvent(int columnIndex, Event event) {
    this.columnIndex = columnIndex;
    this.event = event;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<HeaderDoubleClickHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }
  
  public int getColumnIndex() {
    return columnIndex;
  }

  public Event getEvent() {
    return event;
  }

  @Override
  protected void dispatch(HeaderDoubleClickHandler handler) {
    handler.onHeaderDoubleClick(this);
  }
}