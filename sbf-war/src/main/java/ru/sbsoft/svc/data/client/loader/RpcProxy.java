/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.client.loader;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.data.shared.loader.DataProxy;

/**
 * <code>DataProxy</code> implementation that retrieves data using GWT RPC.
 * 
 * @param <C> the type of data used to configure the load from the proxy
 * @param <D> the type of data being returned by the data proxy
 */
public abstract class RpcProxy<C, D> implements DataProxy<C, D> {

  /**
   * Retrieves data using GWT RPC.
   * 
   * @param loadConfig the load config describing the data to retrieve
   * @param callback the callback to invoke on success or failure
   */
  public abstract void load(C loadConfig, AsyncCallback<D> callback);

  @Override
  public final void load(C loadConfig, final Callback<D, Throwable> callback) {
    load(loadConfig, new AsyncCallback<D>() {
      @Override
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      @Override
      public void onSuccess(D result) {
        callback.onSuccess(result);
      }
    });
  }
}
