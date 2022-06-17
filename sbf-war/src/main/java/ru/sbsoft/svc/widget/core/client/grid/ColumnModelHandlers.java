/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.ColumnMoveEvent.ColumnMoveHandler;
import ru.sbsoft.svc.widget.core.client.event.ColumnMoveEvent.HasColumnMoveHandlers;
import ru.sbsoft.svc.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import ru.sbsoft.svc.widget.core.client.event.ColumnWidthChangeEvent.HasColumnWidthChangeHandlers;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHeaderChangeEvent.ColumnHeaderChangeHandler;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHeaderChangeEvent.HasColumnHeaderChangeHandlers;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHiddenChangeEvent.ColumnHiddenChangeHandler;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHiddenChangeEvent.HasColumnHiddenChangeHandlers;

public interface ColumnModelHandlers extends ColumnWidthChangeHandler, ColumnMoveHandler, ColumnHiddenChangeHandler,
    ColumnHeaderChangeHandler {

  public interface HasColumnModelHandlers extends HasColumnHiddenChangeHandlers, HasColumnWidthChangeHandlers,
  HasColumnMoveHandlers, HasColumnHeaderChangeHandlers {

    HandlerRegistration addColumnModelHandlers(ColumnModelHandlers handlers);
    
  }
}
