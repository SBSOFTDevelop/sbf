/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.menu.Css3MenuAppearance;

public class SlicedMenuAppearance extends Css3MenuAppearance {

  public interface SlicedMenuResources extends Css3MenuResources {
    @Override
    @Source("SlicedMenu.gss")
    SlicedMenuStyle style();

    @Source("menu-bg.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource background();
  }

  public interface SlicedMenuStyle extends Css3MenuStyle {

  }

  public SlicedMenuAppearance() {
    this(GWT.<SlicedMenuResources>create(SlicedMenuResources.class));

  }

  public SlicedMenuAppearance(SlicedMenuResources resources) {
    this(resources, GWT.<BaseMenuTemplate>create(BaseMenuTemplate.class));
  }

  public SlicedMenuAppearance(SlicedMenuResources resources, BaseMenuTemplate template) {
    super(resources, template);
  }
}
