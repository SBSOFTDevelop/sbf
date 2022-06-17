/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.core.client.gestures.CellGestureAdapter;
import ru.sbsoft.svc.core.client.gestures.GestureRecognizer;
import ru.sbsoft.svc.core.client.gestures.PointerEvents;
import ru.sbsoft.svc.core.client.gestures.PointerEventsSupport;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;
import ru.sbsoft.svc.widget.core.client.cell.HandlerManagerContext;

public abstract class AbstractEventCell<C> extends AbstractCell<C> implements HasHandlers {

  private HandlerManager handlerManager;
  private boolean disableEvents;
  private List<CellGestureAdapter<? extends GestureRecognizer, C>> cellGestureAdapters;

  public AbstractEventCell(Set<String> consumedEvents) {
    super(consumedEvents);
  }

  public AbstractEventCell(String... consumedEvents) {
    super(consumedEvents);
  }

  public void addCellGestureAdapter(CellGestureAdapter<? extends GestureRecognizer, C> cellGestureAdapter) {
    if (cellGestureAdapters == null) {
      cellGestureAdapters = new ArrayList<CellGestureAdapter<? extends GestureRecognizer, C>>();
      Set<String> updated = new HashSet<String>();
      Set<String> consumedEvents = super.getConsumedEvents();
      if (consumedEvents != null) {
        updated.addAll(consumedEvents);
      }
      if (PointerEventsSupport.impl.isSupported()) {
        updated.add(PointerEvents.POINTERDOWN.getEventName());
        updated.add(PointerEvents.POINTERMOVE.getEventName());
        updated.add(PointerEvents.POINTERUP.getEventName());
        updated.add(PointerEvents.POINTERCANCEL.getEventName());
      }
      updated.add(BrowserEvents.TOUCHSTART);
      updated.add(BrowserEvents.TOUCHMOVE);
      updated.add(BrowserEvents.TOUCHCANCEL);
      updated.add(BrowserEvents.TOUCHEND);
      setConsumedEventsInternal(Collections.unmodifiableSet(updated));
    }
    cellGestureAdapter.setDelegate(this);
    cellGestureAdapters.add(cellGestureAdapter);
  }

  public void removeCellGestureAdapter(CellGestureAdapter<? extends GestureRecognizer, C> cellGestureAdapter) {
    if (cellGestureAdapter != null) {
      cellGestureAdapters.remove(cellGestureAdapter);
    }
  }

  public CellGestureAdapter<? extends GestureRecognizer, C> getCellGestureAdapter(int index) {
    if (cellGestureAdapters != null) {
      return cellGestureAdapters.get(index);
    }
    return null;
  }

  public int getCellGestureAdapterCount() {
    return cellGestureAdapters == null ? 0 : cellGestureAdapters.size();
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    String eventType = event.getType();
    if (BrowserEvents.TOUCHSTART.equals(eventType) ||
        BrowserEvents.TOUCHMOVE.equals(eventType) ||
        BrowserEvents.TOUCHCANCEL.equals(eventType) ||
        BrowserEvents.TOUCHEND.equals(eventType)) {
      onTouch(context, parent, value, event, valueUpdater);
    }
  }

    /**
   * Adds this handler to the widget.
   * 
   * @param <H> the type of handler to add
   * @param type the event type
   * @param handler the handler
   * @return {@link HandlerRegistration} used to remove the handler
   */
  public final <H extends EventHandler> HandlerRegistration addHandler(final H handler, GwtEvent.Type<H> type) {
    return ensureHandlers().addHandler(type, handler);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    if (handlerManager != null) {
      handlerManager.fireEvent(event);
    }
  }

  public boolean isDisableEvents() {
    return disableEvents;
  }

  public void setDisableEvents(boolean disableEvents) {
    this.disableEvents = disableEvents;
  }

  /**
   * Creates the {@link HandlerManager} used by this Widget. You can override
   * this method to create a custom {@link HandlerManager}.
   * 
   * @return the {@link HandlerManager} you want to use
   */
  protected HandlerManager createHandlerManager() {
    return new HandlerManager(this);
  }

  protected boolean fireCancellableEvent(Context context, GwtEvent<?> event) {
    if (disableEvents) return true;
    fireEvent(context, event);
    if (event instanceof CancellableEvent) {
      return !((CancellableEvent) event).isCancelled();
    }
    return true;
  }
  
  protected boolean fireCancellableEvent(GwtEvent<?> event) {
    if (disableEvents) return true;
    fireEvent(event);
    if (event instanceof CancellableEvent) {
      return !((CancellableEvent) event).isCancelled();
    }
    return true;
  }
  
  protected void fireEvent(Context context, GwtEvent<?> event) {
    if (context instanceof HandlerManagerContext) {
      ((HandlerManagerContext)context).getHandlerManager().fireEvent(event);
    } else {
      fireEvent(event);
    }
  }

  protected void onTouch(Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    for (int i = 0, len = getCellGestureAdapterCount(); i < len; i++) {
      getCellGestureAdapter(i).handle(context, parent, value, event, valueUpdater);
    }
  }

  /**
   * Ensures the existence of the handler manager.
   * 
   * @return the handler manager
   * */
  HandlerManager ensureHandlers() {
    return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
  }

  private native void setConsumedEventsInternal(Set<String> events) /*-{
      this.@com.google.gwt.cell.client.AbstractCell::consumedEvents = events;
  }-*/;

}
