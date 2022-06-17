/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.menu.CheckMenuItemBaseAppearance;
import ru.sbsoft.svc.theme.blue.client.menu.BlueMenuItemAppearance.BlueMenuItemResources;

public class BlueCheckMenuItemAppearance extends CheckMenuItemBaseAppearance {

  public interface BlueCheckMenuItemResources extends CheckMenuItemResources, BlueMenuItemResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/menu/CheckMenuItem.gss", "BlueCheckMenuItem.gss"})
    BlueCheckMenuItemStyle checkStyle();

  }

  public interface BlueCheckMenuItemStyle extends CheckMenuItemStyle {
  }

  public BlueCheckMenuItemAppearance() {
    this(GWT.<BlueCheckMenuItemResources> create(BlueCheckMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public BlueCheckMenuItemAppearance(BlueCheckMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
