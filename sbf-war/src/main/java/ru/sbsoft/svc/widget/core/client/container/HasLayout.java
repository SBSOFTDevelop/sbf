/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

/**
 * A widget that implements this interface has the ability to lay out its
 * children. This interface provides access to the layout capability without
 * compromising the ability to provide a mock container instance in JRE unit
 * tests.
 */
public interface HasLayout {
  /**
   * Forces a class that implements <code>HasLayout</code> to lay out its child
   * widgets.
   */
  void forceLayout();

  /**
   * Returns true if invoked when a class that implements <code>HasLayout</code>
   * is in the process of laying out it's children. Useful in avoiding recursive
   * lay out operations.
   * 
   * @return true if in the process of performing a lay out operation
   */
  boolean isLayoutRunning();

  /**
   * Returns true if invoked when a class that implements <code>HasLayout</code>
   * is in the process of laying out it's children or has performed a lay out in
   * the past.
   * 
   * @return true if a layout is running or has run, false if a layout has not
   *         yet been performed.
   */
  boolean isOrWasLayoutRunning();
}
