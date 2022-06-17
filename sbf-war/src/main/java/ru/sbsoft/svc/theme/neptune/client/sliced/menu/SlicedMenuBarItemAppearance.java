/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.menu.Css3MenuBarItemAppearance;

public class SlicedMenuBarItemAppearance extends Css3MenuBarItemAppearance {
  public interface SlicedMenuBarItemResources extends Css3MenuBarItemResources {
    @Override
    @Source("SlicedMenuBarItem.gss")
    SlicedMenuBarItemStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("menubaritem-hover.png")
    ImageResource itemOver();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("menubaritem-active.png")
    ImageResource itemActive();
  }

  public interface SlicedMenuBarItemStyle extends Css3MenuBarItemStyle {

  }

  public SlicedMenuBarItemAppearance() {
    this(GWT.<SlicedMenuBarItemResources>create(SlicedMenuBarItemResources.class));
  }

  public SlicedMenuBarItemAppearance(SlicedMenuBarItemResources resources) {
    super(resources);
  }

}
