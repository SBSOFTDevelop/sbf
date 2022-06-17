/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared;

import com.google.gwt.resources.client.ImageResource;

/**
 * Provides a icon for the given object.
 * 
 * @param <M> the target object type
 */
public interface IconProvider<M> {

  /**
   * Returns the icon for the given model.
   * 
   * @param model the target model
   * @return the icon
   */
  ImageResource getIcon(M model);

}
