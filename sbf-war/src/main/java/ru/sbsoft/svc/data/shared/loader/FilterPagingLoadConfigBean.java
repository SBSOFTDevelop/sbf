/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PagingLoadConfigBean} with support for filters.
 */
public class FilterPagingLoadConfigBean extends PagingLoadConfigBean implements FilterPagingLoadConfig {

  private List<FilterConfig> filterConfigs = new ArrayList<FilterConfig>();
  
  @Override
  public List<FilterConfig> getFilters() {
    return filterConfigs;
  }

  @Override
  public void setFilters(List<FilterConfig> filters) {
    this.filterConfigs = filters;
  }

}
