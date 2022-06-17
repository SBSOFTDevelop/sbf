/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

/**
 * Defines the interface for component plugins. The
 * {@link #initPlugin(Component)} must be called to pass the target component to
 * the plugin.
 * 
 * @param <C> the target component type
 */
public interface ComponentPlugin<C extends Component> {

  /**
   * Initializes the plugin.
   * 
   * @param component the target component
   */
  void initPlugin(C component);

}
