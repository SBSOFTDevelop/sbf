/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import ru.sbsoft.svc.data.shared.loader.BeforeLoadEvent.HasBeforeLoadHandlers;
import ru.sbsoft.svc.data.shared.loader.LoadExceptionEvent.HasLoadExceptionHandlers;
import ru.sbsoft.svc.data.shared.loader.LoadExceptionEvent.LoadExceptionHandler;

/**
 * Aggregating handler interface for:
 * 
 * <dl>
 * <dd>{@link BeforeLoadEvent}</b></dd>
 * <dd>{@link LoadExceptionEvent}</b></dd>
 * <dd>{@link LoadEvent}</b></dd>
 * </dl>
 * 
 * @param <C> the type of config to request the data
 * @param <M> the type of data to be loaded
 */
public interface LoaderHandler<C, M> extends BeforeLoadHandler<C>, LoadExceptionHandler<C>, LoadHandler<C, M> {

  /**
   * A loader that implements this interface is a public source of all
   * {@link Loader} events, {@link BeforeLoadEvent}, {@link LoadEvent}, and
   * {@link LoadExceptionEvent}.
   * 
   * @param <C> the type of config to request the data
   * @param <M> the type of data to be loaded
   */
  public interface HasLoaderHandlers<C, M> extends HasLoadHandlers<C, M>, HasLoadExceptionHandlers<C>,
      HasBeforeLoadHandlers<C> {

    /**
     * Adds a {@link LoadEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addLoaderHandler(LoaderHandler<C, M> handler);
  }
}
