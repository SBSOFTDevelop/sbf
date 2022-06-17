/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.menu.MenuBarBaseAppearance;

public class BlueMenuBarAppearance extends MenuBarBaseAppearance {

  public interface BlueMenuBarResources extends MenuBarResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/menu/MenuBar.gss", "BlueMenuBar.gss"})
    BlueMenuBarStyle css();

    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource background();

  }

  public interface BlueMenuBarStyle extends MenuBarStyle {
  }

  public BlueMenuBarAppearance() {
    this(GWT.<BlueMenuBarResources> create(BlueMenuBarResources.class));
  }

  public BlueMenuBarAppearance(BlueMenuBarResources resources) {
    super(resources);
  }

}
