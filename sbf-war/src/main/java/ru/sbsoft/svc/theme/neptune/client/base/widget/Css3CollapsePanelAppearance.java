/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.Style.LayoutRegion;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.CollapsePanel.CollapsePanelAppearance;

/**
 *
 */
public class Css3CollapsePanelAppearance implements CollapsePanelAppearance {

  public interface Css3CollapsePanelResources extends ClientBundle {

    @Source("Css3CollapsePanel.gss")
    Css3CollapsePanelStyle style();

    ThemeDetails theme();
  }

  public interface Css3CollapsePanelStyle extends CssResource {
    String panel();

    String noHeader();

    String iconWrap();

    String textWrap();

    String west();

    String east();

    String north();

    String south();
  }

  private final Css3CollapsePanelResources resources;
  private final Css3CollapsePanelStyle style;

  public Css3CollapsePanelAppearance() {
    this(GWT.<Css3CollapsePanelResources>create(Css3CollapsePanelResources.class));
  }

  public Css3CollapsePanelAppearance(Css3CollapsePanelResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }


  @Override
  public void render(SafeHtmlBuilder sb, LayoutRegion region, boolean header) {
    String cls = style.panel();

    switch (region) {
      case WEST:
        cls += " " + style.west();
        break;
      case EAST:
        cls += " " + style.east();
        break;
      case NORTH:
        cls += " " + style.north();
        break;
      case SOUTH:
        cls += " " + style.south();
        break;
    }

    if (!header) {
      cls += " " + style.noHeader();
    }

    sb.appendHtmlConstant("<div class='" + cls + "'>");
    sb.appendHtmlConstant("<div class='" + style.iconWrap() + "'></div>");
    sb.appendHtmlConstant("<div class='" + style.textWrap() + "'></div>");
    sb.appendHtmlConstant("</div>");
  }

  @Override
  public XElement iconWrap(XElement parent) {
    return parent.selectNode("." + style.iconWrap()).cast();
  }

  @Override
  public XElement textWrap(XElement parent) {
    return parent.selectNode("." + style.textWrap()).cast();
  }

}
