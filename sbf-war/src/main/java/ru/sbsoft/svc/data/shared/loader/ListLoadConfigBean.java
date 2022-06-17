/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.ArrayList;
import java.util.List;

import ru.sbsoft.svc.data.shared.SortInfo;
import ru.sbsoft.svc.data.shared.SortInfoBean;

/**
 * Default <code>ListLoadConfig</code> implementation.
 * 
 * @see ListLoadConfig
 */
@SuppressWarnings("serial")
public class ListLoadConfigBean implements ListLoadConfig {

  private List<SortInfoBean> sortInfo = new ArrayList<SortInfoBean>();

  /**
   * Create a new load config instance.
   */
  public ListLoadConfigBean() {

  }

  /**
   * Create a new load config instance.
   */
  public ListLoadConfigBean(SortInfoBean info) {
    getSortInfo().add(info);
  }

  /**
   * Creates a new load config instance.
   * 
   * @param info the sort information
   */
  public ListLoadConfigBean(List<SortInfo> info) {
    setSortInfo(info);
  }

  @Override
  public List<SortInfoBean> getSortInfo() {
    return sortInfo;
  }

  @Override
  public void setSortInfo(List<? extends SortInfo> info) {
    sortInfo.clear();
    for (SortInfo i : info) {
      if (i instanceof SortInfoBean) {
        sortInfo.add((SortInfoBean) i);
      } else {
        sortInfo.add(new SortInfoBean(i.getSortField(), i.getSortDir()));
      }
    }
  }


}
