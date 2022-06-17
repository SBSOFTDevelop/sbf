/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.draggable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.fx.client.Draggable.DraggableAppearance;

public class DraggableDefaultAppearance implements DraggableAppearance {

  public interface DraggableResources extends ClientBundle {

    @Source("Draggable.gss")
    DraggableStyle style();

  }

  public interface DraggableStyle extends CssResource {

    String cursor();

    String proxy();

  }

  private final DraggableStyle style;
  private String proxyClass;

  public DraggableDefaultAppearance() {
    this(GWT.<DraggableResources> create(DraggableResources.class));
  }

  public DraggableDefaultAppearance(DraggableResources resources) {
    this.style = resources.style();
    StyleInjectorHelper.ensureInjected(style, true);
    proxyClass = style.proxy();
  }

  @Override
  public void addUnselectableStyle(Element element) {
    element.addClassName(CommonStyles.get().unselectable());
    element.addClassName(style.cursor());
  }

  @Override
  public Element createProxy() {
    XElement proxyEl = Document.get().createDivElement().cast();
    proxyEl.setVisibility(false);
    proxyEl.setClassName(proxyClass);
    proxyEl.disableTextSelection(true);
    return proxyEl;
  }

  @Override
  public void removeUnselectableStyle(Element element) {
    element.removeClassName(CommonStyles.get().unselectable());
    element.removeClassName(style.cursor());
  }

  @Override
  public void setProxyStyle(String proxyClass) {
    this.proxyClass = proxyClass;
  }

}
