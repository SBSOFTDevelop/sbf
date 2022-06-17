/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import java.util.Map;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.svc.core.client.ValueProvider;

/**
 * Returns the rendered content for a summary row.
 */
public interface SummaryRenderer<M> {

  /**
   * Returns the html content for the summary row.
   * 
   * @param value the summary calculation
   * @param data the data for the group
   * @return the html content
   */
  public SafeHtml render(Number value, Map<ValueProvider<? super M, ?>, Number> data);

}
