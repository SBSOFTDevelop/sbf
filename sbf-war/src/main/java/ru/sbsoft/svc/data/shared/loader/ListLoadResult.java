/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.io.Serializable;
import java.util.List;

/**
 * Load result interface for list based load results.
 * 
 * @param <D> the data type being returned from the server
 */
public interface ListLoadResult<D> extends Serializable {

  /**
   * Returns the remote data.
   * 
   * @return the data
   */
  public List<D> getData();

}
