/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.ValueProvider;

public class AggregationNumberSummaryRenderer<M, N> extends AggregationNumberFormatRenderer<M> {
  private final SummaryType<N, ? extends Number> summaryType;

  public AggregationNumberSummaryRenderer(NumberFormat format, SummaryType<N, ? extends Number> summaryType) {
    super(format);
    this.summaryType = summaryType;
  }

  public AggregationNumberSummaryRenderer(SummaryType<N, ? extends Number> summaryType) {
    super();
    this.summaryType = summaryType;
  }

  public SummaryType<N, ? extends Number> getSummaryType() {
    return summaryType;
  }

  @Override
  public SafeHtml render(int colIndex, Grid<M> grid) {
    ValueProvider<? super M, N> v = grid.getColumnModel().getValueProvider(colIndex);
    Number n = summaryType.calculate(grid.getStore().getAll(), v);
    return SafeHtmlUtils.fromString(getFormat().format(n));
  }

}
