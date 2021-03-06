/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Handler class for {@link LoadEvent} events.
 */
public interface LoadHandler<C, M> extends EventHandler {

  /**
   * Called after a load operation.
   */
  void onLoad(LoadEvent<C, M> event);

  /**
   * A loader that implements this interface is a public source of
   * {@link LoadEvent} events.
   * 
   * @param <M> the type of data to be loaded
   * @param <C> the type of config to request the data
   */
  public interface HasLoadHandlers<M, C> {

    /**
     * Adds a {@link LoadEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addLoadHandler(LoadHandler<M, C> handler);

  }
}
