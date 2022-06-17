/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import ru.sbsoft.svc.core.client.ValueProvider;

/**
 * The GridViewConfig is used to return a CSS style name for rows in a Grid. See
 * {@link GridView#setViewConfig(GridViewConfig)}.
 */
public interface GridViewConfig<M> {

  /**
   * Returns one to many CSS style names separated by spaces.
   * 
   * @param model the model for the row
   * @param valueProvider the valueProvider for the col
   * @param rowIndex the row index
   * @param colIndex the row index
   * @return the CSS style name(s) separated by spaces.
   */
  public String getColStyle(M model, ValueProvider<? super M, ?> valueProvider, int rowIndex, int colIndex);

  /**
   * Returns one to many CSS style names separated by spaces.
   * 
   * @param model the model for the row
   * @param rowIndex the row index
   * @return the CSS style name(s) separated by spaces.
   */
  public String getRowStyle(M model, int rowIndex);

}
