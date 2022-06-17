/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import java.util.Set;

import ru.sbsoft.svc.data.shared.LabelProvider;

public class LabelProviderSafeHtmlCell<T> extends SimpleSafeHtmlCell<T> {

  public LabelProviderSafeHtmlCell(LabelProvider<? super T> labelProvider) {
    super(new LabelProviderSafeHtmlRenderer<T>(labelProvider));
  }
  
  public LabelProviderSafeHtmlCell(LabelProvider<? super T> labelProvider, Set<String> consumedEvents) {
    super(new LabelProviderSafeHtmlRenderer<T>(labelProvider), consumedEvents);
  }

}