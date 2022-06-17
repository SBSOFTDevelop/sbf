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
import ru.sbsoft.svc.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import ru.sbsoft.svc.widget.core.client.grid.Grid.GridCell;

public final class BeforeStartEditEvent<M> extends GridEditingEvent<M, BeforeStartEditHandler<M>> implements
    CancellableEvent {
  public interface BeforeStartEditHandler<M> extends EventHandler {
    void onBeforeStartEdit(BeforeStartEditEvent<M> event);
  }

  public interface HasBeforeStartEditHandlers<M> extends HasHandlers {
    HandlerRegistration addBeforeStartEditHandler(BeforeStartEditHandler<M> handler);
  }

  private static GwtEvent.Type<BeforeStartEditHandler<?>> TYPE;

  public static GwtEvent.Type<BeforeStartEditHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<BeforeStartEditHandler<?>>();
    }
    return TYPE;
  }

  private boolean cancelled;

  public BeforeStartEditEvent(GridCell editCell) {
    super(editCell);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<BeforeStartEditHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;

  }

  @Override
  protected void dispatch(BeforeStartEditHandler<M> handler) {
    handler.onBeforeStartEdit(this);
  }
}
