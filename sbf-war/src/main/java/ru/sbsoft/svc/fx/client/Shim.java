/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.fx.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Window;
import ru.sbsoft.svc.core.client.dom.Layer;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.core.client.util.Rectangle;
import ru.sbsoft.svc.core.client.util.Util;

/**
 * Supports placing a shim over the client window, and optionally just over
 * iframes.
 */
public class Shim {

  private static final Shim instance = GWT.create(Shim.class);

  public static Shim get() {
    return instance;
  }

  private List<XElement> shims = new ArrayList<XElement>();

  private Shim() {

  }

  /**
   * Creates and covers the area with a Shim. If shimIframes is true will only
   * covers IFrames.
   * 
   * @param shimIframes true if you want to cover only iframes
   */
  public void cover(boolean shimIframes) {
    XElement body = Document.get().getBody().<XElement> cast();
    if (shimIframes) {
      NodeList<Element> elements = body.select("iframe:not(.x-noshim)");
      shim(elements);
      elements = body.select("object:not(.x-noshim)");
      shim(elements);
      elements = body.select("applet:not(.x-noshim)");
      shim(elements);
      elements = body.select("embed:not(.x-noshim)");
      shim(elements);
    } else {
      shims.add(createShim(null, 0, 0, Window.getClientWidth(), Window.getClientHeight()));
    }
  }

  public void setStyleAttribute(String attr, String value) {
    for (XElement shim : shims) {
      shim.getStyle().setProperty(attr, value);
    }
  }

  /**
   * Uncovers and removes the shim.
   */
  public void uncover() {
    while (!shims.isEmpty()) {
      shims.get(0).removeFromParent();
      shims.remove(0);
    }
  }

  protected XElement createShim(Element element, int left, int top, int width, int height) {
    Layer l = new Layer(Document.get().createDivElement().<XElement> cast());
    l.enableShim();

    XElement e = l.getElement();
    e.hide();

    e.addClassName(CommonStyles.get().shim());
    e.setSize(width, height);
    e.setLeftTop(left, top);
    e.setInnerSafeHtml(Util.NBSP_SAFE_HTML);
    Element parent = null;
    if (element != null) {
      parent = element.getParentElement();
    }
    if (parent != null) {
      parent.appendChild(e);
    } else {
      Document.get().getBody().appendChild(e);
    }
    e.getStyle().setDisplay(Display.BLOCK);
    if (element != null) {
      e.setZIndex(element.<XElement> cast().getZIndex() + 1);
    } else {
      e.setZIndex(XDOM.getTopZIndex());
    }
    return e;
  }

  protected void shim(NodeList<Element> elements) {
    for (int i = 0; i < elements.getLength(); i++) {
      XElement e = elements.getItem(i).<XElement> cast();
      Rectangle bounds = e.getBounds(true);
      if (bounds.getHeight() > 0 && bounds.getWidth() > 0 && e.isVisible()) {
        shims.add(createShim(e, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()));
      }
    }
  }
}
