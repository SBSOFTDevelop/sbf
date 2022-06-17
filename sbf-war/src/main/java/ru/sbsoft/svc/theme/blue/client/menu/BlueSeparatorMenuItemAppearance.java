/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.SeparatorMenuItemBaseAppearance;

public class BlueSeparatorMenuItemAppearance extends SeparatorMenuItemBaseAppearance {

  public interface BlueSeparatorMenuItemResources extends ClientBundle, SeparatorMenuItemResources {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/menu/Item.gss",
            "ru/sbsoft/svc/theme/blue/client/menu/BlueItem.gss",
            "ru/sbsoft/svc/theme/base/client/menu/SeparatorMenuItem.gss",
            "BlueSeparatorMenuItem.gss"})
    BlueSeparatorMenuItemStyle style();

    ImageResource itemOver();
  }

  public interface BlueSeparatorMenuItemStyle extends SeparatorMenuItemStyle {
  }

  public BlueSeparatorMenuItemAppearance() {
    this(GWT.<BlueSeparatorMenuItemResources> create(BlueSeparatorMenuItemResources.class),
        GWT.<SeparatorMenuItemTemplate> create(SeparatorMenuItemTemplate.class));
  }

  public BlueSeparatorMenuItemAppearance(BlueSeparatorMenuItemResources resources,
      SeparatorMenuItemTemplate template) {
    super(resources, template);
  }

}
