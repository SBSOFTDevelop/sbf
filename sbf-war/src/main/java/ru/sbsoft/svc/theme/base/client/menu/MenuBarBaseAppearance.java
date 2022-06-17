/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.menu;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.widget.core.client.menu.MenuBar.MenuBarAppearance;

public abstract class MenuBarBaseAppearance implements MenuBarAppearance {

  public interface MenuBarResources {

    MenuBarStyle css();

  }

  public interface MenuBarStyle extends CssResource {

    String menuBar();

  }

  private final MenuBarResources resources;

  public MenuBarBaseAppearance(MenuBarResources resources) {
    this.resources = resources;
    resources.css().ensureInjected();
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.appendHtmlConstant("<div class='" + resources.css().menuBar() + " " + CommonStyles.get().noFocusOutline() + "'></div>");
  }

}
