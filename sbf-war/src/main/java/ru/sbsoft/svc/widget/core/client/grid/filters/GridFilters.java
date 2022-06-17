/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid.filters;

import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.loader.FilterPagingLoadConfig;
import ru.sbsoft.svc.data.shared.loader.Loader;

/**
 * Applies filters to the rows in a grid. For more information, see
 * {@link AbstractGridFilters}.
 * <p/>
 * 
 * @param <M> the model type
 */
public class GridFilters<M> extends AbstractGridFilters<M> {

  /**
   * Creates grid filters that are applied locally. See
   * {@link AbstractGridFilters#AbstractGridFilters()} for more information.
   */
  public GridFilters() {
    super();
  }

  /**
   * Creates grid filters to be applied remotely. See
   * {@link AbstractGridFilters#AbstractGridFilters(Loader)} for more
   * information.
   * 
   * @param loader the remote loader
   */
  public GridFilters(Loader<? extends FilterPagingLoadConfig, ?> loader) {
    super(loader);
  }

  /**
   * Creates grid filters that are applied locally. See
   * {@link AbstractGridFilters#AbstractGridFilters()} for more information.
   *
   * @param appearance the filters appearance
   */
  public GridFilters(GridFiltersAppearance appearance) {
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
  public GridFilters(Loader<? extends FilterPagingLoadConfig, ?> loader, GridFiltersAppearance appearance) {
    super(loader, appearance);
  }

  @Override
  public boolean isLocal() {
    return super.isLocal();
  }

  @Override
  public void setLocal(boolean local) {
    super.setLocal(local);
  }

  @Override
  protected Store<M> getStore() {
    return grid.getStore();
  }

}
