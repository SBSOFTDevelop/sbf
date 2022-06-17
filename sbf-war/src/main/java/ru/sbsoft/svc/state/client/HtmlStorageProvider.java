/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.storage.client.Storage;

/**
 * Simple HTML5 Storage implementation of the state provider.
 */
public class HtmlStorageProvider extends Provider {
  private final Storage storage;

  public HtmlStorageProvider(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void clear(String name) {
    storage.removeItem(name);
  }

  @Override
  public void getValue(String name, Callback<String, Throwable> callback) {
    callback.onSuccess(storage.getItem(name));
  }

  @Override
  public void setValue(String name, String value) {
    storage.setItem(name, value);
  }

}
