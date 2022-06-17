/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.io.Serializable;
import java.util.List;

import ru.sbsoft.svc.data.shared.SortInfo;

/**
 * Load config interface for list based data. Adds support for sort information.
 */
public interface ListLoadConfig extends Serializable {


  /**
   * Returns the sort info.
   */
  List<? extends SortInfo> getSortInfo();

  /**
   * Sets the sort info.
   */
  void setSortInfo(List<? extends SortInfo> info);
}
