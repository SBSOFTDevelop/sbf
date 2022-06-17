/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public final class AggregationSafeHtmlRenderer<M> implements AggregationRenderer<M> {
  protected final SafeHtml html;

  public AggregationSafeHtmlRenderer(String text) {
    this(SafeHtmlUtils.fromString(text));
  }

  public AggregationSafeHtmlRenderer(SafeHtml html) {
    this.html = html;
  }

  @Override
  public final SafeHtml render(int colIndex, Grid<M> grid) {
    return html;
  }

}
