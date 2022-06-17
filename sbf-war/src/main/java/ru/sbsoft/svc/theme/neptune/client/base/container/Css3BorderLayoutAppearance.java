/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer.BorderLayoutAppearance;

/**
 *
 */
public class Css3BorderLayoutAppearance implements BorderLayoutAppearance {


  public interface Css3BorderLayoutResources extends ClientBundle {
    @Source("Css3BorderLayout.gss")
    Css3BorderLayoutStyle style();

    ThemeDetails theme();
  }

  public interface Css3BorderLayoutStyle extends CssResource {
    String container();
  }

  private final Css3BorderLayoutResources resources;
  private final Css3BorderLayoutStyle style;

  public Css3BorderLayoutAppearance() {
    this(GWT.<Css3BorderLayoutResources>create(Css3BorderLayoutResources.class));
  }

  public Css3BorderLayoutAppearance(Css3BorderLayoutResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public XElement getContainerTarget(XElement parent) {
    return parent;
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.appendHtmlConstant("<div class='" + style.container() + "'></div>");
  }

  @Override
  public void onInsert(Widget child) {
  }

  @Override
  public void onRemove(Widget child) {
  }

}
