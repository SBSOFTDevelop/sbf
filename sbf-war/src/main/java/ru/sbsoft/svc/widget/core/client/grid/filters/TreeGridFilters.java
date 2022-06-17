/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid.filters;


import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.loader.FilterPagingLoadConfig;
import ru.sbsoft.svc.data.shared.loader.Loader;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.treegrid.TreeGrid;

/**
 * Applies filters to the rows in a grid. For more information, see
 * {@link AbstractGridFilters}.
 * <p/>
 *
 * @param <M> the model type
 */
public class TreeGridFilters<M> extends AbstractGridFilters<M> {

  /**
   * Creates grid filters that are applied locally. See
   * {@link AbstractGridFilters#AbstractGridFilters()} for more information.
   */
  public TreeGridFilters() {
    super();
  }

  /**
   * Creates grid filters to be applied remotely. See
   * {@link AbstractGridFilters#AbstractGridFilters(Loader)} for more
   * information.
   *
   * @param loader the remote loader
   */
  public TreeGridFilters(Loader<? extends FilterPagingLoadConfig, ?> loader) {
    super(loader);
  }

  /**
   * Creates grid filters that are applied locally. See
   * {@link AbstractGridFilters#AbstractGridFilters()} for more information.
   *
   * @param appearance the filters appearance
   */
  public TreeGridFilters(GridFiltersAppearance appearance) {
    super(appearance);
  }

  /**
   * Creates grid filters to be applied remotely. See
   * {@link AbstractGridFilters#AbstractGridFilters(Loader)} for more
   * information.
   *
   * @param loader the remote loader
   * @param appearance the filters appearance
   */
  public TreeGridFilters(Loader<? extends FilterPagingLoadConfig, ?> loader, GridFiltersAppearance appearance) {
    super(loader, appearance);
  }

  /**
   * Initializes the plugin on the provided Grid instance.
   * Asserts that provided grid is an instance of TreeGrid
   *
   * @param grid - TreeGrid instance to initialize to
   */
  public void initPlugin(Grid<M> grid) {
    assert grid instanceof TreeGrid;
    super.initPlugin(grid);
  }

  @Override
  public boolean isLocal() {
    return super.isLocal();
  }

  @Override
  public void setLocal(boolean local) {
    super.setLocal(local);
  }

  /**
   * Returns the TreeStore used by the TreeGrid filters.
   *
   * @return the store used by the grid filters
   */
  @Override
  protected Store<M> getStore() {
    return ((TreeGrid) grid).getTreeStore();
  }
}
