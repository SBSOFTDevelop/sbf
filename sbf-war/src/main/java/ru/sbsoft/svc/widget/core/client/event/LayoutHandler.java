/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.AfterLayoutEvent.AfterLayoutHandler;
import ru.sbsoft.svc.widget.core.client.event.BeforeLayoutEvent.BeforeLayoutHandler;


public abstract class LayoutHandler implements BeforeLayoutHandler, AfterLayoutHandler {

  @Override
  public void onAfterLayout(AfterLayoutEvent event) {
  }

  @Override
  public void onBeforeLayout(BeforeLayoutEvent event) {
  }
  
  /**
   * A layout that implements this interface is a public source of
   * {@link BeforeLayoutEvent} and {@link AfterLayoutEvent} events.
   */
  public interface HasLayoutHandlers {

    /**
     * Adds a {@link LayoutHandler} handler for {@link AfterLayoutEvent} and
     * {@link BeforeLayoutEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addLayoutHandler(LayoutHandler handler);
  }
}
