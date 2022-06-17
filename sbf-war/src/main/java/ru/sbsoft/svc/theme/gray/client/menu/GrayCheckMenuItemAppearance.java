/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.menu.CheckMenuItemBaseAppearance;
import ru.sbsoft.svc.theme.gray.client.menu.GrayMenuItemAppearance.GrayMenuItemResources;

public class GrayCheckMenuItemAppearance extends CheckMenuItemBaseAppearance {

  public interface GrayCheckMenuItemResources extends CheckMenuItemResources, GrayMenuItemResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/menu/CheckMenuItem.gss", "GrayCheckMenuItem.gss"})
    GrayCheckMenuItemStyle checkStyle();

  }

  public interface GrayCheckMenuItemStyle extends CheckMenuItemStyle {
  }

  public GrayCheckMenuItemAppearance() {
    this(GWT.<GrayCheckMenuItemResources> create(GrayCheckMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public GrayCheckMenuItemAppearance(GrayCheckMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
