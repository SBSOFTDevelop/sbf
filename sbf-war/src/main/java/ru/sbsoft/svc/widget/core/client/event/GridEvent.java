/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import ru.sbsoft.svc.widget.core.client.grid.Grid;

public abstract class GridEvent<H extends EventHandler> extends GwtEvent<H> {
  @Override
  public Grid<?> getSource() {
    return (Grid<?>) super.getSource();
  }
}