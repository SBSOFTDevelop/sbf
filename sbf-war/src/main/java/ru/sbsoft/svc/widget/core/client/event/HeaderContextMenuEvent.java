/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;
import ru.sbsoft.svc.widget.core.client.event.HeaderContextMenuEvent.HeaderContextMenuHandler;
import ru.sbsoft.svc.widget.core.client.menu.Menu;

public final class HeaderContextMenuEvent extends GridEvent<HeaderContextMenuHandler> implements CancellableEvent {

  public interface HasHeaderContextMenuHandlers extends HasHandlers {
    HandlerRegistration addHeaderContextMenuHandler(HeaderContextMenuHandler handler);
  }

  public interface HeaderContextMenuHandler extends EventHandler {
    void onHeaderContextMenu(HeaderContextMenuEvent event);
  }

  private static GwtEvent.Type<HeaderContextMenuHandler> TYPE;

  public static GwtEvent.Type<HeaderContextMenuHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<HeaderContextMenuHandler>();
    }
    return TYPE;
  }

  private int columnIndex;
  private Menu menu;
  private boolean cancelled;

  public HeaderContextMenuEvent(int columnIndex, Menu menu) {
    this.columnIndex = columnIndex;
    this.menu = menu;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<HeaderContextMenuHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public Menu getMenu() {
    return menu;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(HeaderContextMenuHandler handler) {
    handler.onHeaderContextMenu(this);
  }
}