/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.menu.Css3MenuBarAppearance;

public class SlicedMenuBarAppearance extends Css3MenuBarAppearance {
  public interface SlicedMenuBarResources extends Css3MenuBarResources {
    @Override
    @Source("SlicedMenuBar.gss")
    SlicedMenuBarStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("menubar-bg.png")
    ImageResource background();
  }

  public interface SlicedMenuBarStyle extends Css3MenuBarStyle {
  }

  public SlicedMenuBarAppearance() {
    this(GWT.<SlicedMenuBarResources>create(SlicedMenuBarResources.class));
  }

  public SlicedMenuBarAppearance(SlicedMenuBarResources resources) {
    super(resources);
  }
}
