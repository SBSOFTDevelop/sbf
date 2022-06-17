/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client.animation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import ru.sbsoft.svc.core.client.Style.Direction;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Util;

public class SlideOut extends Slide {

  public SlideOut(XElement el, Direction dir) {
    super(el, dir);
  }

  @Override
  public void increase(int v) {
    switch (dir) {
      case LEFT:
        element.getStyle().setMarginLeft(-(oBounds.getWidth() - v), Unit.PX);
        wrapEl.setWidth(v);
        break;
      case UP:
        element.getStyle().setMarginTop(-(oBounds.getHeight() - v), Unit.PX);
        wrapEl.setHeight(v);
        break;
      case DOWN:
        element.setY(v);
        break;
      case RIGHT:
        element.setX(v);
        break;
    }
  }

  @Override
  public void onComplete() {
    element.setVisible(false);
    super.onComplete();
  }

  @Override
  public void onStart() {
    super.onStart();
    overflow = Util.parseOverflow(element.getStyle().getOverflow());
    marginTop = Util.parseInt(element.getStyle().getMarginTop(), 0);
    marginLeft = Util.parseInt(element.getStyle().getMarginLeft(), 0);
    //
    wrapEl = Document.get().createDivElement().cast();
    oBounds = element.wrap(wrapEl);

    int h = oBounds.getHeight();
    int w = oBounds.getWidth();

    wrapEl.setSize(w, h);
    wrapEl.setVisible(true);
    element.setVisible(true);

    switch (dir) {
      case UP:
        from = oBounds.getHeight();
        to = 1;
        break;
      case LEFT:
        from = oBounds.getWidth();
        to = 0;
        break;
      case RIGHT:
        from = wrapEl.getX();
        to = from + wrapEl.getWidth(false);
        break;

      case DOWN:
        from = wrapEl.getY();
        to = from + wrapEl.getHeight(false);
        break;
    }
  }

}