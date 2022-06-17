/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.grid.filters.AbstractGridFilters.GridFiltersAppearance;

public class GridFiltersDefaultAppearance implements GridFiltersAppearance {

  public interface GridFiltersResources extends ClientBundle {
    @Source("GridFilters.gss")
    GridFiltersStyle style();
  }
  
  public interface GridFiltersStyle extends CssResource {
    String filtered();
  }

  private GridFiltersResources resources;
  private GridFiltersStyle style;
  
  public GridFiltersDefaultAppearance() {
    this(GWT.<GridFiltersResources> create(GridFiltersResources.class));
  }

  public GridFiltersDefaultAppearance(GridFiltersResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public String filteredStyle() {
    return style.filtered();
  }
}
