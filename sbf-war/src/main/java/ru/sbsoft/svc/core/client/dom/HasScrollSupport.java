/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom;

/**
 * Interface for objects that provide scroll support.
 *
 * @see ScrollSupport
 */
public interface HasScrollSupport {

  /**
   * Returns the scroll support instance.
   * @return the scroll support instance
   */
  public ScrollSupport getScrollSupport();
  
  /**
   * Sets the scroll support.
   * 
   * @param scrollSupport the scroll support
   */
  public void setScrollSupport(ScrollSupport scrollSupport);
}
