/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import com.google.gwt.core.client.Callback;

/**
 * Defines the interface for objects that can retrieve data.
 *  
 * @param <C> the type of data used to configure the load from the proxy
 * @param <D> the type of data being returned by the data proxy
 */
public interface DataProxy<C, D> {

  /**
   * Data should be retrieved using the specified load config. When specified,
   * the <code>DataReader</code> can be used to "process" the raw data.
   * 
   * @param loadConfig the load config object to be passed to server
   * @param callback the data callback
   */
  public void load(C loadConfig, Callback<D, Throwable> callback);

}
