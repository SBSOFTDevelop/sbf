/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import com.google.gwt.user.client.ui.Widget;

/**
 * Implemented by containers that support an active child widget.
 */
public interface HasActiveWidget {

  /**
   * Returns the active widget.
   * 
   * @return the active widget
   */
  Widget getActiveWidget();

  /**
   * Sets the active widget.
   * 
   * @param active the widget
   */
  void setActiveWidget(Widget active);

}
