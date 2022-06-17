/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.menu.MenuBar.MenuBarAppearance;

public class Css3MenuBarAppearance implements MenuBarAppearance {
  public interface Css3MenuBarResources extends ClientBundle {
    @Source("Css3MenuBar.gss")
    Css3MenuBarStyle style();

    ThemeDetails theme();
  }

  public interface Css3MenuBarStyle extends CssResource {
    String menuBar();
  }

  public interface MenuBarTemplate extends XTemplates {
    @XTemplate("<div class='{cssClasses}'></div>")
    SafeHtml render(String cssClasses);
  }

  private final Css3MenuBarStyle style;
  private final MenuBarTemplate template;

  public Css3MenuBarAppearance() {
    this(GWT.<Css3MenuBarResources>create(Css3MenuBarResources.class));
  }

  public Css3MenuBarAppearance(Css3MenuBarResources resources) {
    this(resources, GWT.<MenuBarTemplate>create(MenuBarTemplate.class));
  }

  public Css3MenuBarAppearance(Css3MenuBarResources resources, MenuBarTemplate template) {
    this.template = template;
    this.style = resources.style();
    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render(style.menuBar() + " " + CommonStyles.get().noFocusOutline()));
  }
}
