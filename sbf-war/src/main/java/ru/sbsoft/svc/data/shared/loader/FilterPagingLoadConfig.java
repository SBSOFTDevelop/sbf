/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.List;

/**
 * A {@link PagingLoadConfig} with support for filters.
 */
public interface FilterPagingLoadConfig extends PagingLoadConfig {

  /**
   * Returns the list of filters for this load config.
   * 
   * @return the list of filters
   */
  // TODO possible move the filters to ListLoadConfig to not force paging
  List<FilterConfig> getFilters();

  /**
   * Sets the list of filters for this load config.
   * 
   * @param filters the list of filters
   */
  void setFilters(List<FilterConfig> filters);
}
