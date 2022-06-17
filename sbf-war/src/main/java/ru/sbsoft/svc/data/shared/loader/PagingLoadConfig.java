/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;


/**
 * A <code>ListLoadConfig</code> with support for limit and offset values.
 */
public interface PagingLoadConfig extends ListLoadConfig {

  /**
   * Sets the limit.
   * 
   * @param limit the limit
   */
  public void setLimit(int limit);

  /**
   * Sets the offset.
   * 
   * @param offset the offset
   */
  public void setOffset(int offset);

  /**
   * The number of records being requested.
   */
  public int getLimit();

  /**
   * The offset for the first record to retrieve.
   */
  public int getOffset();
}
