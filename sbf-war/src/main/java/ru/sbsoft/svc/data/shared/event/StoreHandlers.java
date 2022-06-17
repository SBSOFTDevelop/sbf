/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.event;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent.HasStoreAddHandlers;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent.StoreAddHandler;
import ru.sbsoft.svc.data.shared.event.StoreClearEvent.HasStoreClearHandler;
import ru.sbsoft.svc.data.shared.event.StoreClearEvent.StoreClearHandler;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent.HasStoreDataChangeHandlers;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent.HasStoreFilterHandlers;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import ru.sbsoft.svc.data.shared.event.StoreRecordChangeEvent.HasStoreRecordChangeHandlers;
import ru.sbsoft.svc.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import ru.sbsoft.svc.data.shared.event.StoreRemoveEvent.HasStoreRemoveHandler;
import ru.sbsoft.svc.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import ru.sbsoft.svc.data.shared.event.StoreSortEvent.HasStoreSortHandler;
import ru.sbsoft.svc.data.shared.event.StoreSortEvent.StoreSortHandler;
import ru.sbsoft.svc.data.shared.event.StoreUpdateEvent.HasStoreUpdateHandlers;
import ru.sbsoft.svc.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;

/**
 * Handler interface for common store events.
 * 
 * @param <M> the type of data contained in the store
 */
public interface StoreHandlers<M> extends StoreAddHandler<M>, StoreRemoveHandler<M>, StoreFilterHandler<M>,
    StoreClearHandler<M>, StoreUpdateHandler<M>, StoreDataChangeHandler<M>, StoreRecordChangeHandler<M>,
    StoreSortHandler<M> {

  /**
   * A class that implements this interface is a public source of
   * common store events.
   * 
   * @param <M> the type of data contained in the store
   */
  public interface HasStoreHandlers<M> extends HasStoreAddHandlers<M>, HasStoreRemoveHandler<M>,
      HasStoreUpdateHandlers<M>, HasStoreRecordChangeHandlers<M>, HasStoreFilterHandlers<M>, HasStoreClearHandler<M>,
      HasStoreDataChangeHandlers<M>, HasStoreSortHandler<M> {

    /**
     * Adds a common store event handler.
     * 
     * @param handlers the handlers
     * @return the registration for the event
     */
    HandlerRegistration addStoreHandlers(StoreHandlers<M> handlers);
  }
}