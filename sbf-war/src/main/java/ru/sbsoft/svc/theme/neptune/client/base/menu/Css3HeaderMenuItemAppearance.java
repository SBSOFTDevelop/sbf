/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.menu.HeaderMenuItem.HeaderMenuItemAppearance;

public class Css3HeaderMenuItemAppearance implements HeaderMenuItemAppearance {

  public interface Css3HeaderMenuItemResources extends ClientBundle {
    @Source("Css3HeaderMenuItem.gss")
    Css3HeaderMenuItemStyle headerStyle();

    ThemeDetails theme();
  }

  public interface Css3HeaderMenuItemStyle extends CssResource {

    public String menuText();

  }

  private Css3HeaderMenuItemStyle style;

  public Css3HeaderMenuItemAppearance() {
    this(GWT.<Css3HeaderMenuItemResources>create(Css3HeaderMenuItemResources.class));
  }

  public Css3HeaderMenuItemAppearance(Css3HeaderMenuItemResources resources) {
    this.style = resources.headerStyle();
    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public void applyItemStyle(Element element) {
    element.addClassName(style.menuText());
  }

  @Override
  public void onActivate(XElement parent) {
  }

  @Override
  public void onDeactivate(XElement parent) {
  }
}
