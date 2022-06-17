/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Renderer for aggregation rows in a grid.
 * 
 * @param <M> the model type
 */
public interface AggregationRenderer<M> {

  /**
   * Returns the rendered HTML or Widget for the given cell.
   * 
   * @param colIndex the column index
   * @param grid the containing grid
   */
  public SafeHtml render(int colIndex, Grid<M> grid);

}
