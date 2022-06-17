/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.button;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.button.ButtonGroup.ButtonGroupAppearance;

public class Css3ButtonGroupAppearance implements ButtonGroupAppearance {

  public interface Css3ButtonGroupResources extends ClientBundle {
    @Source("Css3ButtonGroup.gss")
    Css3ButtonGroupStyle style();

    ThemeDetails theme();
  }

  public interface Css3ButtonGroupStyle extends CssResource {
    String group();

    String body();

    String header();
  }

  public interface Css3ButtonGroupTemplate extends XTemplates {
    @XTemplate("<div class='{style.group}'><div class='{style.header}'></div><div class='{style.body}'></div></div>")
    SafeHtml render(Css3ButtonGroupStyle style);
  }

  private final Css3ButtonGroupResources resources;
  private final Css3ButtonGroupStyle style;
  private final Css3ButtonGroupTemplate template;

  public Css3ButtonGroupAppearance() {
    this(GWT.<Css3ButtonGroupResources> create(Css3ButtonGroupResources.class));
  }

  public Css3ButtonGroupAppearance(Css3ButtonGroupResources resources) {
    this.resources = resources;
    style = resources.style();
    template = GWT.create(Css3ButtonGroupTemplate.class);

    StyleInjectorHelper.ensureInjected(resources.style(), true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void setHeading(XElement parent, SafeHtml html) {
    getHeaderElement(parent).setInnerSafeHtml(html);
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    getHeaderElement(parent).setVisible(!hide);
  }

  @Override
  public XElement getHeaderElement(XElement parent) {
    return parent.getFirstChildElement().cast();
  }

  @Override
  public XElement getContentElem(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public int getFrameHeight(XElement parent) {
    return resources.theme().buttonGroup().borderRadius() * 2;
  }

  @Override
  public int getFrameWidth(XElement parent) {
    return resources.theme().buttonGroup().borderRadius() * 2;
  }

}
