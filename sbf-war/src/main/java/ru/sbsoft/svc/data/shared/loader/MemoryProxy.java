/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import com.google.gwt.core.client.Callback;

/**
 * A <code>DataProxy</code> implementation that simply passes the data specified
 * in the constructor to the reader when its load method is called.
 */
public class MemoryProxy<C, D> implements DataProxy<C, D> {

  private D data;

  /**
   * Creates new memory proxy.
   * 
   * @param data the local data
   */
  public MemoryProxy(D data) {
    this.data = data;
  }

  /**
   * Returns the proxy data.
   * 
   * @return the data
   */
  public D getData() {
    return data;
  }

  @Override
  public void load(C loadConfig, Callback<D, Throwable> callback) {
    try {
      callback.onSuccess(data);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  /**
   * Sets the proxy data.
   * 
   * @param data the data
   */
  public void setData(D data) {
    this.data = data;
  }

}
