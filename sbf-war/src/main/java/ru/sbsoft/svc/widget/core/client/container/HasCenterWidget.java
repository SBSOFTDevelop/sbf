/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that implements this interface contains a widget in the "center"
 * position. This interface provides access to that widget, if it exists,
 * without compromising the ability to provide a mock container instance in JRE
 * unit tests.
 * 
 * @see BorderLayoutContainer
 */
public interface HasCenterWidget {
  /**
   * Returns the center widget or null if one has not been set.
   * 
   * @return the center widget or null if one has not been set
   */
  Widget getCenterWidget();

  /**
   * Sets the center widget, replacing any existing center widget.
   * 
   * @param widget the new widget to place in the center position of the
   *          container
   */
  void setCenterWidget(IsWidget widget);
}
