/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.menu.MenuBarItemBaseAppearance;

public class BlueMenuBarItemAppearance extends MenuBarItemBaseAppearance {

  public interface BlueMenuBarItemResources extends MenuBarItemResources, ClientBundle {
    @Source({"ru/sbsoft/svc/theme/base/client/menu/MenuBarItem.gss", "BlueMenuBarItem.gss"})
    BlueMenuBarItemStyle css();
  }
  
  public interface BlueMenuBarItemStyle extends MenuBarItemStyle {
  }
  
  public BlueMenuBarItemAppearance() {
    this(GWT.<BlueMenuBarItemResources>create(BlueMenuBarItemResources.class));
  }
  
  public BlueMenuBarItemAppearance(BlueMenuBarItemResources resources) {
    super(resources);
  }

}
