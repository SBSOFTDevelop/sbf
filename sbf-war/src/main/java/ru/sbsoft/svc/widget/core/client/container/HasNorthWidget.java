/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that implements this interface contains a widget in the "north"
 * position. This interface provides access to that widget, if it exists,
 * without compromising the ability to provide a mock container instance in JRE
 * unit tests.
 * 
 * @see BorderLayoutContainer
 */
public interface HasNorthWidget {
  /**
   * Returns the north widget or null if one has not been set.
   * 
   * @return the north widget or null if one has not been set
   */
  Widget getNorthWidget();

  /**
   * Sets the north widget, replacing any existing north widget.
   * 
   * @param widget the new widget to place in the north position of the
   *          container
   */
  void setNorthWidget(IsWidget widget);
}
