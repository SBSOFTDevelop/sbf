/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

/**
 * Interface for objects that can be collapsed.
 */
public interface Collapsible {

  /**
   * Collapses the widget.
   */
  void collapse();

  /**
   * Expands the widget.
   */
  void expand();

  /**
   * Returns true if the widget is expanded.
   * 
   * @return true for expanded
   */
  boolean isExpanded();
}
