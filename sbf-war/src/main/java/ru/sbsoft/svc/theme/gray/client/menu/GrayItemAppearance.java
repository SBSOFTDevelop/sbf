/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.menu.ItemBaseAppearance;

public class GrayItemAppearance extends ItemBaseAppearance {

  public interface GrayItemResources extends ItemBaseAppearance.ItemResources, ClientBundle {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/menu/Item.gss",
            "ru/sbsoft/svc/theme/gray/client/menu/GrayItem.gss",
            "GrayItem.gss"})
    GrayItemStyle style();

    ImageResource itemOver();

  }

  public interface GrayItemStyle extends ItemStyle {

  }

  public GrayItemAppearance() {
    this(GWT.<GrayItemResources> create(GrayItemResources.class));
  }

  public GrayItemAppearance(GrayItemResources resources) {
    super(resources);
  }

}
