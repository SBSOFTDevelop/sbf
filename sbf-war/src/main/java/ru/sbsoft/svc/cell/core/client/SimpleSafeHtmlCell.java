/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import java.util.Set;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public class SimpleSafeHtmlCell<C> extends AbstractSafeHtmlCell<C> {

  public SimpleSafeHtmlCell(SafeHtmlRenderer<C> renderer, Set<String> consumedEvents) {
    super(renderer, consumedEvents);
  }

  public SimpleSafeHtmlCell(SafeHtmlRenderer<C> renderer, String... consumedEvents) {
    super(renderer, consumedEvents);
  }

  @Override
  protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
    if (data != null) {
      sb.append(data);
    }
  }

}
