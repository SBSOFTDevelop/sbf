/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

import com.google.gwt.resources.client.ImageResource;

/**
 * Implemented by objects that have icons. It allows the current value of the
 * icon to be set or retrieved.
 */
public interface HasIcon {

  /**
   * Returns the icon.
   * 
   * @return the icon
   */
  ImageResource getIcon();

  /**
   * Sets the icon.
   * 
   * @param icon the icon
   */
  void setIcon(ImageResource icon);

}
