/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.Style.Direction;
import ru.sbsoft.svc.core.client.Style.LayoutRegion;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.SplitBar.SplitBarAppearance;

public class SplitBarDefaultAppearance implements SplitBarAppearance {

  public interface SplitBarResources extends ClientBundle {
    @Source("SplitBar.gss")
    SplitBarStyle css();
    
    ImageResource miniBottom();
    
    ImageResource miniLeft();
    
    ImageResource miniRight();
    
    ImageResource miniTop();
  }

  public interface SplitBarStyle extends CssResource {
    String bar();

    String horizontalBar();

    String mini();

    String miniBottom();

    String miniLeft();

    String miniOver();

    String miniRight();

    String miniTop();

    String proxy();
    
    String verticalBar();
  }

  private final SplitBarResources resources;
  private final SplitBarStyle style;

  public SplitBarDefaultAppearance() {
    this(GWT.<SplitBarResources> create(SplitBarResources.class));
  }

  public SplitBarDefaultAppearance(SplitBarResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
    this.style.ensureInjected();
  }

  @Override
  public int getDefaultBarWidth() {
    return 5;
  }

  @Override
  public String miniClass(Direction direction) {
    String cls = style.mini();
    
    switch (direction) {
      case UP:
        cls += " " + style.miniTop();
        break;
      case DOWN:
        cls += " " + style.miniBottom();
        break;
      case LEFT:
        cls += " " + style.miniLeft();
        break;
      case RIGHT:
        cls += " " + style.miniRight();
        break;
    }
    
    return cls;
  }

  @Override
  public String miniSelector() {
    return "." + style.mini();
  }

  @Override
  public void onMiniOver(XElement mini, boolean over) {
    mini.setClassName(style.miniOver(), over);
  }

  @Override
  public String proxyClass() {
    return style.proxy();
  }

  @Override
  public void render(SafeHtmlBuilder sb, LayoutRegion region) {
    String cls = "";
    if (region == LayoutRegion.SOUTH || region == LayoutRegion.NORTH) {
      cls = style.horizontalBar();
    } else {
      cls = style.verticalBar();
    }
    sb.appendHtmlConstant("<div class='" + cls + "'></div>");
  }

}
