/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.MenuItemBaseAppearance;

public class GrayMenuItemAppearance extends MenuItemBaseAppearance {

  public interface GrayMenuItemResources extends MenuItemBaseAppearance.MenuItemResources, ClientBundle {

    ImageResource menuParent();

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/menu/Item.gss",
            "ru/sbsoft/svc/theme/gray/client/menu/GrayItem.gss",
            "ru/sbsoft/svc/theme/base/client/menu/MenuItem.gss",
            "GrayMenuItem.gss"})
    GrayMenuItemStyle style();

    ImageResource itemOver();
  }

  public interface GrayMenuItemStyle extends MenuItemBaseAppearance.MenuItemStyle {
  }

  public GrayMenuItemAppearance() {
    this(GWT.<GrayMenuItemResources> create(GrayMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public GrayMenuItemAppearance(GrayMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
