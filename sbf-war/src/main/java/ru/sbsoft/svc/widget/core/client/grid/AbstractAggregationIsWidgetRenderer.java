/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.safehtml.shared.SafeHtml;

public abstract class AbstractAggregationIsWidgetRenderer<M> implements AggregationRenderer<M> {

  @Override
  public final SafeHtml render(int colIndex, Grid<M> grid) {
    return null;
  }

}
