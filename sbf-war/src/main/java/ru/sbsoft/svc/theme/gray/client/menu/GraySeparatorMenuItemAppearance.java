/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.SeparatorMenuItemBaseAppearance;

public class GraySeparatorMenuItemAppearance extends SeparatorMenuItemBaseAppearance {

  public interface GraySeparatorMenuItemResources extends ClientBundle, SeparatorMenuItemResources {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/menu/Item.gss",
            "ru/sbsoft/svc/theme/gray/client/menu/GrayItem.gss",
            "ru/sbsoft/svc/theme/base/client/menu/SeparatorMenuItem.gss",
            "GraySeparatorMenuItem.gss"})
    GraySeparatorMenuItemStyle style();

    ImageResource itemOver();
  }

  public interface GraySeparatorMenuItemStyle extends SeparatorMenuItemStyle {
  }

  public GraySeparatorMenuItemAppearance() {
    this(GWT.<GraySeparatorMenuItemResources> create(GraySeparatorMenuItemResources.class),
        GWT.<SeparatorMenuItemTemplate> create(SeparatorMenuItemTemplate.class));
  }

  public GraySeparatorMenuItemAppearance(GraySeparatorMenuItemResources resources,
      SeparatorMenuItemTemplate template) {
    super(resources, template);
  }

}
