/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import ru.sbsoft.svc.data.shared.LabelProvider;

public class LabelProviderSafeHtmlRenderer<T> extends AbstractSafeHtmlRenderer<T> {

  private final LabelProvider<? super T> labelProvider;

  public LabelProviderSafeHtmlRenderer(LabelProvider<? super T> labelProvider) {
    this.labelProvider = labelProvider;
  }

  @Override
  public SafeHtml render(T object) {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    sb.appendEscaped(labelProvider.getLabel(object));
    return sb.toSafeHtml();
  }

}
