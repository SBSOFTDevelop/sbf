/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Point;
import ru.sbsoft.svc.widget.core.client.event.ArrowSelectEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeSelectEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.XEvent;

public class SplitButtonCell extends TextButtonCell {

  public SplitButtonCell() {
    this(GWT.<ButtonCellAppearance<String>> create(ButtonCellAppearance.class));
  }

  public SplitButtonCell(ButtonCellAppearance<String> appearance) {
    super(appearance);
  }

  @Override
  protected void onClick(Context context, XElement p, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
    event.preventDefault();

    if (!isDisableEvents() && fireCancellableEvent(context, new BeforeSelectEvent(context))) {

      if (isClickOnArrow(p, event)) {
        if (menu != null && !menu.isVisible()) {
          showMenu(p);
        }
        fireEvent(context, new ArrowSelectEvent(context, menu));
      } else {
        fireEvent(context, new SelectEvent(context));
      }
    }
  }

  public boolean isClickOnArrow(XElement p, NativeEvent e) {
    Point eventXY = e.<XEvent>cast().getXY();

    Point elementXY = p.getPosition(false);

    int buttonX = elementXY.getX();
    int buttonY = elementXY.getY();
    int width = p.getOffsetWidth();
    int height = p.getOffsetHeight();

    int x = eventXY.getX();
    int y = eventXY.getY();

    return (getArrowAlign() == ButtonArrowAlign.BOTTOM) ? y > (buttonY + height  - 14)
        : x > (buttonX + width - 14);
  }
}
