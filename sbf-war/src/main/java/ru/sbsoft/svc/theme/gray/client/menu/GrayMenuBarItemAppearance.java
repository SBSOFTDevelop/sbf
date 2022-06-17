/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.menu.MenuBarItemBaseAppearance;

public class GrayMenuBarItemAppearance extends MenuBarItemBaseAppearance {

  public interface GrayMenuBarItemResources extends MenuBarItemResources, ClientBundle {
    @Source({"ru/sbsoft/svc/theme/base/client/menu/MenuBarItem.gss", "GrayMenuBarItem.gss"})
    GrayMenuBarItemStyle css();
  }
  
  public interface GrayMenuBarItemStyle extends MenuBarItemStyle {
  }
  
  public GrayMenuBarItemAppearance() {
    this(GWT.<GrayMenuBarItemResources>create(GrayMenuBarItemResources.class));
  }
  
  public GrayMenuBarItemAppearance(GrayMenuBarItemResources resources) {
    super(resources);
  }

}
