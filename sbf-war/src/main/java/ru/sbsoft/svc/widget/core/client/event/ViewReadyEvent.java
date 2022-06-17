/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.widget.core.client.event.ViewReadyEvent.ViewReadyHandler;

public final class ViewReadyEvent extends GridEvent<ViewReadyHandler> {
  public interface HasViewReadyHandlers extends HasHandlers {
    HandlerRegistration addViewReadyHandler(ViewReadyHandler handler);
  }

  public interface ViewReadyHandler extends EventHandler {
    void onViewReady(ViewReadyEvent event);
  }

  private static GwtEvent.Type<ViewReadyHandler> TYPE;

  public static GwtEvent.Type<ViewReadyHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<ViewReadyHandler>();
    }
    return TYPE;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<ViewReadyHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }

  @Override
  protected void dispatch(ViewReadyHandler handler) {
    handler.onViewReady(this);
  }
}