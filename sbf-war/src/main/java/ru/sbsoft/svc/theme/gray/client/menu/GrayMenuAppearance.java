/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.MenuBaseAppearance;

public class GrayMenuAppearance extends MenuBaseAppearance {

  public interface GrayMenuResources extends MenuBaseAppearance.MenuResources, ClientBundle {

    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Vertical)
    ImageResource itemOver();

    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Vertical)
    ImageResource menu();

    ImageResource miniBottom();

    ImageResource miniTop();

    @Source({"ru/sbsoft/svc/theme/base/client/menu/Menu.gss", "GrayMenu.gss"})
    GrayMenuStyle style();

  }

  public interface GrayMenuStyle extends MenuStyle {
  }

  public GrayMenuAppearance() {
    this(GWT.<GrayMenuResources> create(GrayMenuResources.class), GWT.<BaseMenuTemplate> create(BaseMenuTemplate.class));
  }

  public GrayMenuAppearance(GrayMenuResources resources, BaseMenuTemplate template) {
    super(resources, template);
  }

}
