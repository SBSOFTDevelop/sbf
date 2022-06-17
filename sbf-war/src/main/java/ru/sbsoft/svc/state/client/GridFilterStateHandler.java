/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import java.util.ArrayList;
import java.util.List;

import ru.sbsoft.svc.data.shared.event.StoreFilterEvent;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import ru.sbsoft.svc.data.shared.loader.FilterConfig;
import ru.sbsoft.svc.data.shared.loader.FilterPagingLoadConfig;
import ru.sbsoft.svc.data.shared.loader.LoadEvent;
import ru.sbsoft.svc.data.shared.loader.LoadHandler;
import ru.sbsoft.svc.data.shared.loader.Loader;
import ru.sbsoft.svc.data.shared.loader.PagingLoadResult;
import ru.sbsoft.svc.state.client.GridFilterStateHandler.GridFilterState;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.filters.Filter;
import ru.sbsoft.svc.widget.core.client.grid.filters.GridFilters;

public class GridFilterStateHandler<M> extends ComponentStateHandler<GridFilterState, Grid<M>> {

  public interface GridFilterState {
    List<FilterConfig> getFilters();

    void setFilters(List<FilterConfig> filters);
  }

  private class Handler implements StoreFilterHandler<M>, LoadHandler<FilterPagingLoadConfig, PagingLoadResult<M>> {
    @Override
    public void onFilter(StoreFilterEvent<M> event) {
      GridFilterStateHandler.this.onFilter(event);
    }

    @Override
    public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<M>> event) {
      GridFilterStateHandler.this.onLoad(event);
    }

  }

  protected Grid<M> grid;
  protected GridFilters<M> filters;
  protected Loader<FilterPagingLoadConfig, ?> loader;

  public GridFilterStateHandler(Grid<M> grid, GridFilters<M> filters) {
    super(GridFilterState.class, grid);

    if (!filters.isLocal()) {
      assert filters.getLoader() != null : "Loader must not be null with remote filtering";
    }

    this.grid = grid;
    this.filters = filters;
    this.loader = filters.getLoader();

    init(grid, filters);
  }

  @Override
  public void applyState() {
    if (getObject().isStateful()) {
      GridFilterState state = getState();
      List<FilterConfig> stateConfigs = state.getFilters();
      if (stateConfigs == null) return;

      List<ColumnConfig<M, ?>> columns = grid.getColumnModel().getColumns();
      for (int i = 0; i < columns.size(); i++) {
        ColumnConfig<M, ?> col = grid.getColumnModel().getColumn(i);
        Filter<M, ?> filter = filters.getFilter(col.getPath());
        if (filter != null) {
          List<FilterConfig> configs = findConfigs(col.getPath(), stateConfigs);
          if (configs == null) {
            configs = new ArrayList<FilterConfig>();
          }
          filter.setFilterConfig(configs);
        }
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void init(Grid<M> component, GridFilters<M> filters) {
    Handler handler = new Handler();
    if (filters.isLocal()) {
      component.getStore().addStoreFilterHandler(handler);
    } else {
      loader.addLoadHandler((LoadHandler) handler);
    }
  }

  protected void onFilter(StoreFilterEvent<M> event) {
    if (!getObject().isStateful()) {
      return;
    }
    saveFilters();
  }

  protected void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<M>> event) {
    if (!getObject().isStateful()) {
      return;
    }
    saveFilters();
  }

  protected void saveFilters() {
    GridFilterState state = getState();
    List<FilterConfig> stateConfigs = state.getFilters();
    if (stateConfigs == null) {
      stateConfigs = new ArrayList<FilterConfig>();
      state.setFilters(stateConfigs);
    }

    stateConfigs.clear();

    List<ColumnConfig<M, ?>> columns = grid.getColumnModel().getColumns();
    for (int i = 0; i < columns.size(); i++) {
      if (grid.getColumnModel().isHidden(i)) {
        continue;
      }
      ColumnConfig<M, ?> col = grid.getColumnModel().getColumn(i);
      Filter<M, ?> filter = filters.getFilter(col.getPath());
      if (filter != null && filter.isActive()) {
        List<FilterConfig> configs = filter.getFilterConfig();
        if (configs == null || configs.size() == 0) {
          continue;
        }
        for (int j = 0; j < configs.size(); j++) {
          FilterConfig actual = configs.get(j);

          FilterConfig stateConfig = StateManager.get().getDefaultStateInstance(FilterConfig.class);
          stateConfig.setField(col.getPath());
          stateConfig.setType(actual.getType());
          stateConfig.setComparison(actual.getComparison());
          stateConfig.setValue(actual.getValue());

          stateConfigs.add(stateConfig);
        }

      }
    }

    saveState();
  }

  private List<FilterConfig> findConfigs(String path, List<FilterConfig> configs) {
    List<FilterConfig> matches = new ArrayList<FilterConfig>();
    for (int i = 0; i < configs.size(); i++) {
      FilterConfig config = configs.get(i);
      if (path != null && path.equals(config.getField())) {
        matches.add(config);
      }
    }
    return matches;
  }
}
