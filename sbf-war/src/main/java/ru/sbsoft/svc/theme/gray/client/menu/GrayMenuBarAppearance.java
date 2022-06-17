/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.menu.MenuBarBaseAppearance;

public class GrayMenuBarAppearance extends MenuBarBaseAppearance {

  public interface GrayMenuBarResources extends MenuBarResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/menu/MenuBar.gss", "GrayMenuBar.gss"})
    GrayMenuBarStyle css();

    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource background();

  }

  public interface GrayMenuBarStyle extends MenuBarStyle {
  }

  public GrayMenuBarAppearance() {
    this(GWT.<GrayMenuBarResources> create(GrayMenuBarResources.class));
  }

  public GrayMenuBarAppearance(GrayMenuBarResources resources) {
    super(resources);
  }

}
