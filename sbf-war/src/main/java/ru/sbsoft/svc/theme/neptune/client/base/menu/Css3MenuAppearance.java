/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.MenuBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3MenuAppearance extends MenuBaseAppearance {
  public interface Css3MenuResources extends MenuResources, ClientBundle {
    @Override
    @Source("Css3Menu.gss")
    Css3MenuStyle style();

    ImageResource miniTop();

    ImageResource miniBottom();

    ThemeDetails theme();
  }

  public interface Css3MenuStyle extends MenuStyle {

  }

  public Css3MenuAppearance() {
    this(GWT.<Css3MenuResources>create(Css3MenuResources.class));
  }

  public Css3MenuAppearance(Css3MenuResources resources) {
    this(resources, GWT.<BaseMenuTemplate>create(MenuBaseAppearance.BaseMenuTemplate.class));
  }

  public Css3MenuAppearance(Css3MenuResources resources, BaseMenuTemplate template) {
    super(resources, template);
  }
}
