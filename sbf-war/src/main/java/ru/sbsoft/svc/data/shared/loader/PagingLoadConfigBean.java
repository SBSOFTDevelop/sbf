/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

/**
 * Default implementation of the <code>PagingLoadConfig</code> interface.
 */
public class PagingLoadConfigBean extends ListLoadConfigBean implements PagingLoadConfig {

  private int limit;
  private int offset;

  /**
   * Creates a new paging load config.
   */
  public PagingLoadConfigBean() {
    this(0, 50);
  }

  /**
   * Creates a new paging load config.
   * 
   * @param offset the offset
   * @param limit the limit
   */
  public PagingLoadConfigBean(int offset, int limit) {
    setOffset(offset);
    setLimit(limit);
  }

  public int getLimit() {
    return limit;
  }

  public int getOffset() {
    return offset;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

}
