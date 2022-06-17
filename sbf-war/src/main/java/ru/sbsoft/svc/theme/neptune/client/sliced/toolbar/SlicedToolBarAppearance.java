/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.toolbar.Css3ToolBarAppearance;

/**
 *
 */
public class SlicedToolBarAppearance extends Css3ToolBarAppearance {

  public interface SlicedToolBarResources extends Css3ToolBarResources {
    @Source({"ru/sbsoft/svc/theme/base/client/container/BoxLayout.gss", "ru/sbsoft/svc/theme/neptune/client/base/container/Css3HBoxLayoutContainer.gss", "SlicedToolBar.gss"})
    @Override
    Css3ToolBarStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource background();
  }

  public SlicedToolBarAppearance() {
    this(GWT.<SlicedToolBarResources>create(SlicedToolBarResources.class));
  }

  public SlicedToolBarAppearance(SlicedToolBarResources resources) {
    super(resources);
  }
}
