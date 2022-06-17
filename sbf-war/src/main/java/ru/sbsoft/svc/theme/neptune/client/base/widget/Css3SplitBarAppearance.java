/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.Style.Direction;
import ru.sbsoft.svc.core.client.Style.LayoutRegion;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.SplitBar.SplitBarAppearance;

/**
 *
 */
public class Css3SplitBarAppearance implements SplitBarAppearance {

  private Css3SplitBarResources resources;

  public interface Css3SplitBarResources extends ClientBundle {
    @Source("Css3SplitBar.gss")
    Css3SplitBarStyle style();

    ThemeDetails theme();

    ImageResource miniBottom();

    ImageResource miniLeft();

    ImageResource miniRight();

    ImageResource miniTop();
  }

  public interface Css3SplitBarStyle extends CssResource {
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

  private final Css3SplitBarStyle style;

  public Css3SplitBarAppearance() {
    this(GWT.<Css3SplitBarResources>create(Css3SplitBarResources.class));
  }

  public Css3SplitBarAppearance(Css3SplitBarResources resources) {
    this.resources = resources;
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public int getDefaultBarWidth() {
    return resources.theme().splitbar().handleWidth();
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
