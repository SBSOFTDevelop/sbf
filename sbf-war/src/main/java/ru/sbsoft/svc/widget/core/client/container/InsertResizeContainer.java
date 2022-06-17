/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * A base class for layout containers that can insert widgets and honor the
 * {@link RequiresResize} contract.
 */
public abstract class InsertResizeContainer extends ResizeContainer implements InsertPanel.ForIsWidget {
  @Override
  public void insert(IsWidget w, int beforeIndex) {
    insert(asWidgetOrNull(w), beforeIndex);
  }

  @Override
  public void insert(Widget w, int beforeIndex) {
    super.insert(w, beforeIndex);
  }
}