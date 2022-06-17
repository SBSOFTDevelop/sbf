/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import ru.sbsoft.svc.widget.core.client.grid.Grid.GridCell;

public final class CompleteEditEvent<M> extends GridEditingEvent<M, CompleteEditHandler<M>> {
  public interface CompleteEditHandler<M> extends EventHandler {
    void onCompleteEdit(CompleteEditEvent<M> event);
  }

  public interface HasCompleteEditHandlers<M> extends HasHandlers {
    HandlerRegistration addCompleteEditHandler(CompleteEditHandler<M> handler);
  }

  private static GwtEvent.Type<CompleteEditHandler<?>> TYPE;

  public static GwtEvent.Type<CompleteEditHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<CompleteEditHandler<?>>();
    }
    return TYPE;
  }

  public CompleteEditEvent(GridCell editCell) {
    super(editCell);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<CompleteEditHandler<M>> getAssociatedType() {
    return (GwtEvent.Type) getType();
  }

  @Override
  protected void dispatch(CompleteEditHandler<M> handler) {
    handler.onCompleteEdit(this);
  }
}
