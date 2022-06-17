/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.state.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.SortDir;
import ru.sbsoft.svc.data.shared.Store.StoreSortInfo;
import ru.sbsoft.svc.state.client.GridStateHandler.GridState;
import ru.sbsoft.svc.widget.core.client.event.ColumnWidthChangeEvent;
import ru.sbsoft.svc.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import ru.sbsoft.svc.widget.core.client.event.SortChangeEvent;
import ru.sbsoft.svc.widget.core.client.event.SortChangeEvent.SortChangeHandler;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHiddenChangeEvent;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHiddenChangeEvent.ColumnHiddenChangeHandler;
import ru.sbsoft.svc.widget.core.client.grid.Grid;

/**
 * State handler for grids
 * 
 * @param <M> the grid model type
 */
public class GridStateHandler<M> extends ComponentStateHandler<GridState, Grid<M>> {

  public interface GridSortState {
    SortDir getSortDir();

    String getSortField();

    void setSortDir(SortDir dir);

    void setSortField(String sortField);
  }

  public interface GridState {
    Set<String> getHidden();

    SortDir getSortDir();

    String getSortField();

    Map<String, Integer> getWidths();

    void setHidden(Set<String> hidden);

    void setSortDir(SortDir sortDir);

    void setSortField(String field);

    void setWidths(Map<String, Integer> widths);
  }

  private class Handler implements ColumnHiddenChangeHandler, ColumnWidthChangeHandler, SortChangeHandler {

    @Override
    public void onColumnHiddenChange(ColumnHiddenChangeEvent event) {
      handleColumnHiddenChange(event);
    }

    @Override
    public void onColumnWidthChange(ColumnWidthChangeEvent event) {
      handleColumnWidthChange(event);
    }

    @Override
    public void onSortChange(SortChangeEvent event) {
      handleSortChange(event);
    }

  }

  /**
   * Creates a new grid state handler instance.
   * 
   * @param stateType the state type
   * @param component the grid
   * @param key the state key
   */
  public GridStateHandler(Class<GridState> stateType, Grid<M> component, String key) {
    super(stateType, component, key);

    init(component);
  }

  /**
   * Creates a nbew grid state handler instance.
   * 
   * @param component the grid
   */
  public GridStateHandler(Grid<M> component) {
    super(GridState.class, component);

    init(component);
  }

  /**
   * Creates a new state handler instance.
   * 
   * @param component the grid
   * @param key the state key
   */
  public GridStateHandler(Grid<M> component, String key) {
    super(GridState.class, component, key);

    init(component);
  }

  @Override
  public void applyState() {
    if (getObject().isStateful()) {
      GridState state = getState();
      Set<String> hidden = state.getHidden();
      if (hidden != null) {
        for (String path : hidden) {
          ColumnConfig<M, ?> column = getObject().getColumnModel().findColumnConfig(path);
          if (column != null) {
            column.setHidden(true);
          }
        }
      }

      Map<String, Integer> widths = state.getWidths();
      if (widths != null) {
        for (String path : widths.keySet()) {
          ColumnConfig<M, ?> column = getObject().getColumnModel().findColumnConfig(path);
          if (column != null) {
            column.setWidth(widths.get(path));
          }
        }
      }

      if (state.getSortField() != null) {
        ColumnConfig<M, ?> column = getObject().getColumnModel().findColumnConfig(
                state.getSortField());
        if (column != null) {
          getObject().getStore().clearSortInfo();
          getObject().getStore().addSortInfo(createStoreSortInfo(getObject().getStore(), column, state.getSortDir()));
        }
      }
    }
  }

  /**
   * @see ru.sbsoft.svc.widget.core.client.grid.GridView#createStoreSortInfo(ru.sbsoft.svc.widget.core.client.grid.ColumnConfig, ru.sbsoft.svc.data.shared.SortDir)
   */
  @SuppressWarnings("unchecked")
  protected <V> StoreSortInfo<M> createStoreSortInfo(ListStore<M> ds, ColumnConfig<M, V> column, SortDir sortDir) {
    if (column.getComparator() == null) {
      // These casts can fail, but that implies that the data model has changed, in which case app should deal with invalid state
      @SuppressWarnings("rawtypes")
      ValueProvider<M, Comparable> vp = (ValueProvider) column.getValueProvider();
      return new StoreSortInfo<M>(ds.wrapRecordValueProvider(vp), sortDir);
    } else {
      return new StoreSortInfo<M>(ds.wrapRecordValueProvider(column.getValueProvider()), column.getComparator(), sortDir);
    }
  }

  protected void handleColumnHiddenChange(ColumnHiddenChangeEvent event) {
    if (getObject().isStateful()) {
      GridState state = getState();
      Set<String> hidden = state.getHidden();
      if (hidden == null) {
        hidden = new HashSet<String>();
        state.setHidden(hidden);
      }

      if (event.getColumnConfig().isHidden()) {
        hidden.add(event.getColumnConfig().getPath());
      } else {
        hidden.remove(event.getColumnConfig().getPath());
      }

      saveState();
    }
  }

  protected void handleColumnWidthChange(ColumnWidthChangeEvent event) {
    if (getObject().isStateful()) {
      GridState state = getState();
      Map<String, Integer> widths = state.getWidths();
      if (widths == null) {
        widths = new HashMap<String, Integer>();
        state.setWidths(widths);
      }
      widths.put(event.getColumnConfig().getPath(), event.getColumnConfig().getWidth());

      saveState();
    }
  }

  protected void handleSortChange(SortChangeEvent event) {
    if (getObject().isStateful()) {
      GridState state = getState();

      state.setSortField(event.getSortInfo().getSortField());
      state.setSortDir(event.getSortInfo().getSortDir());

      saveState();
    }
  }

  protected void init(Grid<M> component) {
    Handler h = new Handler();
    component.addSortChangeHandler(h);
    component.getColumnModel().addColumnHiddenChangeHandler(h);
    component.getColumnModel().addColumnWidthChangeHandler(h);
  }

}
