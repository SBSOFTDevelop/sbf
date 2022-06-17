/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.shared.HandlerManager;
import ru.sbsoft.svc.core.shared.event.CancellableEvent;

/**
 * Fires before a selection occurs.
 * 
 * @param <T> the type about to be selected
 */
public class CellBeforeSelectionEvent<T> extends BeforeSelectionEvent<T> implements CancellableEvent {

  public static <T> CellBeforeSelectionEvent<T> fire(HandlerManager manager, Context context, T item) {
    if (manager.isEventHandled(BeforeSelectionEvent.getType())) {
      CellBeforeSelectionEvent<T> event = new CellBeforeSelectionEvent<T>(context, item);
      event.setItem(item);
      if (manager != null) {
        manager.fireEvent(event);
      }
      return event;
    }
    return null;
  }
  
  public static <T> CellBeforeSelectionEvent<T> fire(HasBeforeSelectionHandlers<T> handler, Context context, T item) {
    CellBeforeSelectionEvent<T> event = new CellBeforeSelectionEvent<T>(context, item);
    handler.fireEvent(event);
    return event;
  }

  private Context context;

  /**
   * Creates a new before selection event.
   * 
   * @param context the cell context
   * @param item the item to be selected
   */
  protected CellBeforeSelectionEvent(Context context, T item) {
    this.context = context;
    this.setItem(item);
  }

  /**
   * Returns the cell context if event fired from cell
   * 
   * @return the cell context if event fired via cell or null
   */
  public Context getContext() {
    return context;
  }

  @Override
  public boolean isCancelled() {
    return isCanceled();
  }

  @Override
  public void setCancelled(boolean cancel) {
    if (cancel) {
      cancel();
    }
  }

}
