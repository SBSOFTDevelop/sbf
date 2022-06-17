/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.info;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.info.DefaultInfoConfig.DefaultInfoConfigAppearance;

public class Css3DefaultInfoConfigAppearance implements DefaultInfoConfigAppearance {
  interface Style extends CssResource {
    String header();

    String message();
  }

  interface Resources extends ClientBundle {
    @Source("infoConfig.gss")
    Style infoConfig();

    ThemeDetails theme();
  }

  interface Template extends XTemplates {
    @XTemplate("<div class='{style.header}'>{title}</div><div class='{style.message}'>{message}</div>")
    SafeHtml render(Style style, SafeHtml title, SafeHtml message);
  }

  private Template template;
  private Style style;

  public Css3DefaultInfoConfigAppearance() {
    this(GWT.<Resources>create(Resources.class));
  }

  public Css3DefaultInfoConfigAppearance(Resources resources) {
    this(resources, GWT.<Template>create(Template.class));
  }

  public Css3DefaultInfoConfigAppearance(Resources resources, Template template) {
    this.template = template;
    this.style = resources.infoConfig();
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder sb, SafeHtml title, SafeHtml message) {
    sb.append(template.render(style, title, message));
  }
}
