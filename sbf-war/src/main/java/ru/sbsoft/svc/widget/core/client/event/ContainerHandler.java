/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.AddEvent.AddHandler;
import ru.sbsoft.svc.widget.core.client.event.AddEvent.HasAddHandlers;
import ru.sbsoft.svc.widget.core.client.event.BeforeAddEvent.BeforeAddHandler;
import ru.sbsoft.svc.widget.core.client.event.BeforeAddEvent.HasBeforeAddHandlers;
import ru.sbsoft.svc.widget.core.client.event.BeforeRemoveEvent.BeforeRemoveHandler;
import ru.sbsoft.svc.widget.core.client.event.BeforeRemoveEvent.HasBeforeRemoveHandlers;
import ru.sbsoft.svc.widget.core.client.event.RemoveEvent.HasRemoveHandlers;
import ru.sbsoft.svc.widget.core.client.event.RemoveEvent.RemoveHandler;


/**
 * Aggregating handler interface for:
 * 
 * <dl>
 * <dd>{@link BeforeAddEvent}</b></dd>
 * <dd>{@link AddEvent}</b></dd>
 * <dd>{@link BeforeRemoveEvent}</b></dd>
 * <dd>{@link RemoveEvent}</b></dd>
 * </dl>
 */
public interface ContainerHandler extends BeforeAddHandler, AddHandler, BeforeRemoveHandler,
    RemoveHandler {

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeAddEvent}, {@link AddEvent}, {@link BeforeRemoveEvent},
   * {@link RemoveEvent} events.
   */
  public interface HasContainerHandlers extends HasBeforeAddHandlers,
          HasAddHandlers, HasBeforeRemoveHandlers, HasRemoveHandlers {

      /**
       * Adds a {@link ContainerHandler} handler for {@link BeforeAddEvent} ,
       * {@link AddEvent}, {@link BeforeRemoveEvent}, {@link RemoveEvent} events.
       * 
       * @param handler
       *            the handler
       * @return the registration for the event
       */
      HandlerRegistration addContainerHandler(ContainerHandler handler);
  }
}
