/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * Abstract state handler for Components, capable of using the widget's stateId
 * property as a key instead of being given one.
 * 
 * Will emit a warning in hosted mode (and if enabled, in production mode) if
 * the stateId is generated, as this might change between page loads, or as the
 * application changes.
 * 
 * @param <S> the state type
 * @param <C> the component
 */
public abstract class ComponentStateHandler<S, C extends Component> extends AbstractStateHandler<S, C> {

  private static Logger logger = Logger.getLogger(ComponentStateHandler.class.getName());

  public ComponentStateHandler(Class<S> stateType, C component) {
    super(stateType, component, component.getStateId());

    if (!GWT.isProdMode() && component.getStateId().startsWith("x-widget-")) {
      logger.warning(component.getStateId() + " State handler given a widget with a generated state id, this can result in state being incorrectly applied as generated ids change");
    }
  }

  public ComponentStateHandler(Class<S> stateType, C component, String key) {
    super(stateType, component, key);
  }

}
