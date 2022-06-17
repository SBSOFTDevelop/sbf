/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.cell;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.shared.HandlerManager;

public class DefaultHandlerManagerContext extends Context implements HandlerManagerContext {

  protected HandlerManager handlerManager;
  
  public DefaultHandlerManagerContext(int index, int column, Object key, HandlerManager handlerManager) {
    super(index, column, key);
    this.handlerManager = handlerManager;
  }
  
  @Override
  public HandlerManager getHandlerManager() {
    return handlerManager;
  }

}
