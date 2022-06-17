/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.MenuItemBaseAppearance;

public class BlueMenuItemAppearance extends MenuItemBaseAppearance {

  public interface BlueMenuItemResources extends MenuItemBaseAppearance.MenuItemResources, ClientBundle {

    ImageResource menuParent();

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/menu/Item.gss",
            "ru/sbsoft/svc/theme/blue/client/menu/BlueItem.gss",
            "ru/sbsoft/svc/theme/base/client/menu/MenuItem.gss",
            "BlueMenuItem.gss"})
    BlueMenuItemStyle style();

    ImageResource itemOver();
  }

  public interface BlueMenuItemStyle extends MenuItemBaseAppearance.MenuItemStyle {
  }

  public BlueMenuItemAppearance() {
    this(GWT.<BlueMenuItemResources> create(BlueMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public BlueMenuItemAppearance(BlueMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
