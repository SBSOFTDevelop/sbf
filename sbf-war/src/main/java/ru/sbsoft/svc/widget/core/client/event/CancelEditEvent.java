/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import ru.sbsoft.svc.widget.core.client.grid.Grid.GridCell;

public final class CancelEditEvent<M> extends GridEditingEvent<M, CancelEditHandler<M>> {
  public interface CancelEditHandler<M> extends EventHandler {
    void onCancelEdit(CancelEditEvent<M> event);
  }

  public interface HasCancelEditHandlers<M> extends HasHandlers {
    HandlerRegistration addCancelEditHandler(CancelEditHandler<M> handler);
  }

  private static GwtEvent.Type<CancelEditHandler<?>> TYPE;

  public static GwtEvent.Type<CancelEditHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<CancelEditHandler<?>>();
    }
    return TYPE;
  }

  public CancelEditEvent(GridCell editCell) {
    super(editCell);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<CancelEditHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  protected void dispatch(CancelEditHandler<M> handler) {
    handler.onCancelEdit(this);
  }
}
