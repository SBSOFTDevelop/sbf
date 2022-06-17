/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractRpcProvider extends Provider {

  @Override
  public void getValue(String name, final Callback<String, Throwable> callback) {
    getValue(name, new AsyncCallback<String>() {
      @Override
      public void onSuccess(String result) {
        callback.onSuccess(result);
      }

      @Override
      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }
    });
  }

  public abstract void getValue(String name, AsyncCallback<String> callback);

}
