/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.i18n.client.NumberFormat;

public abstract class AggregationNumberFormatRenderer<M> implements AggregationRenderer<M> {
  private final NumberFormat format;

  public AggregationNumberFormatRenderer() {
    this(NumberFormat.getDecimalFormat());
  }

  public AggregationNumberFormatRenderer(NumberFormat format) {
    this.format = format;
  }

  protected NumberFormat getFormat() {
    return format;
  }
}
