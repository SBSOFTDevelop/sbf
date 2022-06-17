/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import ru.sbsoft.svc.data.shared.Store;

/**
 * Abstract supertype for events that come from a store, making it clear that
 * the source of the event is a Store.
 * 
 * @param <M> the model type
 * @param <H> the event handler type
 */
public abstract class StoreEvent<M, H extends EventHandler> extends GwtEvent<H> {
  @SuppressWarnings("unchecked")
  @Override
  public Store<M> getSource() {
    return (Store<M>) super.getSource();
  }
}