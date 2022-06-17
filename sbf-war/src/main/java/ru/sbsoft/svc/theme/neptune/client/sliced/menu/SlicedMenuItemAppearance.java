/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.menu;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.menu.Css3MenuItemAppearance;

public class SlicedMenuItemAppearance extends Css3MenuItemAppearance {
  public interface SlicedMenuItemResources extends Css3MenuItemResources {

    @Override
    @Source("SlicedMenuItem.gss")
    SlicedMenuItemStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("menuitem-hover.png")
    ImageResource itemOver();
  }

  public interface SlicedMenuItemStyle extends Css3MenuItemStyle {
  }

  public SlicedMenuItemAppearance() {
    this(GWT.<SlicedMenuItemResources>create(SlicedMenuItemResources.class));
  }

  public SlicedMenuItemAppearance(SlicedMenuItemResources resources) {
    this(resources, GWT.<MenuItemTemplate>create(MenuItemTemplate.class));
  }

  public SlicedMenuItemAppearance(SlicedMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }
}
