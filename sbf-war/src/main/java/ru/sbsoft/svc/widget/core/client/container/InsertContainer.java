/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * A base class for layout containers that can insert widgets.
 */
public abstract class InsertContainer extends Container implements InsertPanel.ForIsWidget {
  @Override
  public void insert(IsWidget w, int beforeIndex) {
    insert(asWidgetOrNull(w), beforeIndex);
  }

  @Override
  public void insert(Widget w, int beforeIndex) {
    super.insert(w, beforeIndex);
  }

  @Override
  protected void setElement(Element elem) {
    super.setElement(elem);
    //EXTGWT-3759
    // margins on the child widgets can cause the container element to be offset by the margin size
    // overflow is set in the widget directly, not the appearance, to ensure overflow is set
    // overflow is set after setElement to allow the value to be changed after construction and before
    // the component is attached
    getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
  }
}