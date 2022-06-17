/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.window;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.sliced.panel.SlicedHeaderAppearance;

public class SlicedWindowHeaderAppearance extends SlicedHeaderAppearance {
  public interface SlicedWindowHeaderResources extends SlicedHeaderResources {
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss",
            "ru/sbsoft/svc/theme/neptune/client/base/panel/Css3Header.gss",
            "ru/sbsoft/svc/theme/neptune/client/sliced/panel/SlicedHeader.gss"})
    @Override
    SlicedWindowHeaderStyle style();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("window-header-background.png")
    ImageResource headerBackground();
  }

  public interface SlicedWindowHeaderStyle extends SlicedHeaderStyle {
  }

  public SlicedWindowHeaderAppearance() {
    super(GWT.<SlicedWindowHeaderResources>create(SlicedWindowHeaderResources.class));
  }
}
