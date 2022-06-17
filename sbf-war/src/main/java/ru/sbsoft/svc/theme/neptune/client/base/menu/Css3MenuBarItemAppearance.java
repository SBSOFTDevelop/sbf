/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.menu.MenuBarItemBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.menu.MenuBarItem.MenuBarItemAppearance;

public class Css3MenuBarItemAppearance extends MenuBarItemBaseAppearance implements MenuBarItemAppearance {
  public interface Css3MenuBarItemResources extends MenuBarItemResources, ClientBundle {
    @Override
    @Source("Css3MenuBarItem.gss")
    Css3MenuBarItemStyle css();

    ThemeDetails theme();
  }

  public interface Css3MenuBarItemStyle extends MenuBarItemStyle {

  }

  public Css3MenuBarItemAppearance() {
    this(GWT.<Css3MenuBarItemResources>create(Css3MenuBarItemResources.class));
  }

  public Css3MenuBarItemAppearance(Css3MenuBarItemResources resources) {
    super(resources);
  }
}
