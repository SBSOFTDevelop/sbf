/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.tips;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.Style.Side;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.tips.Tip.TipAppearance;

public class Css3TipAppearance implements TipAppearance {
  public interface Css3TipResources extends ClientBundle {
    @Source("Css3Tip.gss")
    Css3TipStyle style();

    ImageResource anchorBottom();

    ImageResource anchorLeft();

    ImageResource anchorRight();

    ImageResource anchorTop();

    ThemeDetails theme();
  }
  public interface Css3TipStyle extends CssResource {
    String tip();

    String tipWrap();

    String anchorTop();

    String anchorRight();

    String anchorBottom();

    String anchorLeft();

    String anchor();

    String heading();

    String headingWrap();

    String text();

    String textWrap();

    String tools();
  }

  public interface Css3TipTemplate extends XTemplates {
    @XTemplate("<div class='{style.tipWrap}'><div class='{style.tip}'>" +
        "<div class='{style.tools}'></div>" +
        "<div class='{style.headingWrap}'><span class='{style.heading}'></span></div>" +
        "<div class='{style.textWrap}'><span class='{style.text}'></span></div>" +
        "</div></div>")
    SafeHtml render(Css3TipStyle style);
  }

  private final Css3TipResources resources;
  private final Css3TipStyle style;
  private final Css3TipTemplate template;

  public Css3TipAppearance() {
    this(GWT.<Css3TipResources>create(Css3TipResources.class));
  }

  public Css3TipAppearance(Css3TipResources resources) {
    this(resources, GWT.<Css3TipTemplate>create(Css3TipTemplate.class));
  }

  public Css3TipAppearance(Css3TipResources resources, Css3TipTemplate template) {
    this.template = template;
    this.resources = resources;
    style = resources.style();
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void applyAnchorDirectionStyle(XElement anchorEl, Side anchor) {
    anchorEl.setClassName(style.anchorTop(), anchor == Side.TOP);
    anchorEl.setClassName(style.anchorBottom(), anchor == Side.BOTTOM);
    anchorEl.setClassName(style.anchorRight(), anchor == Side.RIGHT);
    anchorEl.setClassName(style.anchorLeft(), anchor == Side.LEFT);
  }

  @Override
  public void applyAnchorStyle(XElement anchorEl) {
    anchorEl.addClassName(style.anchor());
  }

  public XElement getHeaderElement(XElement parent) {
    return parent.selectNode("." + style.heading());
  }

  @Override
  public XElement getBodyElement(XElement parent) {
    return parent.selectNode("." + style.text());
  }

  @Override
  public XElement getToolsElement(XElement parent) {
    return parent.selectNode("." + style.tools());
  }

  @Override
  public void removeAnchorStyle(XElement anchorEl) {
    anchorEl.removeClassName(style.anchor());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void updateContent(XElement parent, SafeHtml title, SafeHtml body) {
    XElement header = getHeaderElement(parent);
    if (title == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      header.getParentElement().getStyle().setDisplay(Display.NONE);
    } else {
      header.setInnerSafeHtml(title);
      header.getParentElement().getStyle().clearDisplay();
    }

    getBodyElement(parent).setInnerSafeHtml(body);
  }
}
