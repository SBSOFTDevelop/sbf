/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.info.Info.InfoAppearance;

public class Css3InfoAppearance implements InfoAppearance {
  interface Template extends XTemplates {
    @XTemplate("<div class='{infoWrap}'><div class='{info}'></div></div>")
    SafeHtml render(Styles styles);
  }
  interface Styles extends CssResource {
    String infoWrap();
    String info();
  }
  interface Resources extends ClientBundle {

    @Source("info.gss")
    Styles info();
    ThemeDetails theme();
  }

  private final Template template;
  private final Styles styles;

  public Css3InfoAppearance() {
    this(GWT.<Resources>create(Resources.class));

  }

  public Css3InfoAppearance(Resources resources) {
    this(resources, GWT.<Template>create(Template.class));
  }

  public Css3InfoAppearance(Resources resources, Template template) {
    this.template = template;
    this.styles = resources.info();
    StyleInjectorHelper.ensureInjected(styles, true);
  }

  @Override
  public XElement getContentElement(XElement parent) {
    return parent.getFirstChildElement().getFirstChildElement().cast();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(styles));
  }
}
