/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared;

import java.io.Serializable;

import ru.sbsoft.svc.core.client.ValueProvider;

/**
 * Aggregates sort field and sort direction.
 */
public class SortInfoBean implements Serializable, SortInfo {

  private String sortField;
  private SortDir sortDir = SortDir.ASC;

  /**
   * Creates a new sort field instance.
   */
  public SortInfoBean() {

  }

  /**
   * Creates a new sort info instance.
   * 
   * @param field the sort field
   * @param sortDir the sort direction
   */
  public SortInfoBean(String field, SortDir sortDir) {
    this.sortField = field;
    this.sortDir = sortDir;
  }

  /**
   * Creates a new sort info instance.
   * 
   * @param valueProvider the value provider for the sort field
   * @param sortDir the sort direction
   */
  public SortInfoBean(ValueProvider<?, ?> valueProvider, SortDir sortDir) {
    this.sortField = valueProvider.getPath();
    this.sortDir = sortDir;
  }

  /**
   * Returns the sort field.
   * 
   * @return the sort field
   */
  @Override
  public String getSortField() {
    return sortField;
  }

  /**
   * Sets the sort field.
   * 
   * @param sortField the sort field
   */
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Returns the sort direction.
   * 
   * @return the sort direction
   */
  @Override
  public SortDir getSortDir() {
    return sortDir;
  }

  /**
   * Sets the sort direction.
   * 
   * @param sortDir the sort direction
   */
  public void setSortDir(SortDir sortDir) {
    this.sortDir = sortDir;
  }

}
