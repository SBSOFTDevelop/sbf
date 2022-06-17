/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * Aggregates sort field and sort direction.
 */
@ProxyFor(SortInfoBean.class)
public interface SortInfo extends ValueProxy {

  /**
   * Returns the sort direction.
   * 
   * @return the sort direction
   */
  SortDir getSortDir();

  /**
   * Returns the sort field.
   * 
   * @return the sort field
   */
  String getSortField();

  /**
   * Sets the sort direction.
   * 
   * @param sortDir the sort direction
   */
  void setSortDir(SortDir sortDir);

  /**
   * Sets the sort field.
   * 
   * @param sortField the sort field
   */
  void setSortField(String sortField);
}
