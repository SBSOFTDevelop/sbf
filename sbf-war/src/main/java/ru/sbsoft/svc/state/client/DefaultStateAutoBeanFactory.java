/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import ru.sbsoft.svc.state.client.BorderLayoutStateHandler.BorderLayoutState;
import ru.sbsoft.svc.state.client.GridFilterStateHandler.GridFilterState;
import ru.sbsoft.svc.state.client.GridStateHandler.GridState;
import ru.sbsoft.svc.state.client.TreeStateHandler.TreeState;

/**
 * Default <code>AutoBeanFactory</code> used by the {@link StateManager}. The
 * auto bean factory is specified using a module rule:
 * &lt;set-configuration-property name="SVC.state.autoBeanFactory"
 * value="ru.sbsoft.svc.state.client.DefaultStateAutoBeanFactory" />.
 * 
 * <p />
 * To add additional beans to the factory, this interface should be extended.
 * The new interface should then be specified in your applications module file
 * to 'override' the current rule.
 */
public interface DefaultStateAutoBeanFactory extends AutoBeanFactory {
  
  AutoBean<TreeState> tree();

  AutoBean<BorderLayoutState> borderLayout();

  AutoBean<GridState> grid();

  AutoBean<GridFilterState> gridFilter();
}
