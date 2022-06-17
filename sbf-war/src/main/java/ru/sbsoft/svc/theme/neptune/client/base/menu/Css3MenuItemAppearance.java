/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.MenuItemBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3MenuItemAppearance extends MenuItemBaseAppearance {

  public interface Css3MenuItemResources extends MenuItemResources, ClientBundle {

    @Override
    @Source("Css3MenuItem.gss")
    Css3MenuItemStyle style();

    ThemeDetails theme();

    ImageResource activeMenuParent();

    ImageResource menuParent();
  }

  public interface Css3MenuItemStyle extends MenuItemStyle {

  }

  public Css3MenuItemAppearance() {
    this(GWT.<Css3MenuItemResources>create(Css3MenuItemResources.class));
  }

  public Css3MenuItemAppearance(Css3MenuItemResources resources) {
    this(resources, GWT.<MenuItemTemplate>create(MenuItemTemplate.class));
  }

  public Css3MenuItemAppearance(Css3MenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }
}
