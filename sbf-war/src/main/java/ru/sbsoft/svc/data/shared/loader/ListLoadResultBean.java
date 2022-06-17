/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.List;

/**
 * Default implementation of the <code>ListLoadResult</code> interface.
 * 
 * @param <Data> the result data type
 */
public class ListLoadResultBean<Data> implements ListLoadResult<Data> {

  /**
   * The remote data.
   */
  protected List<Data> list;

  /**
   * Creates a new list load result.
   */
  public ListLoadResultBean() {

  }

  /**
   * Creates a new list load result.
   * 
   * @param list the data
   */
  public ListLoadResultBean(List<Data> list) {
    this.list = list;
  }

  @Override
  public List<Data> getData() {
    return list;
  }

  /**
   * Sets the data for the list load result.
   * 
   * @param list the data for this list load result
   */
  public void setData(List<Data> list) {
    this.list = list;
  }

}
