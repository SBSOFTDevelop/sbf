/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance;

/**
 *
 */
public class SlicedHeaderAppearance extends Css3HeaderAppearance {

  public interface SlicedHeaderResources extends Css3HeaderResources {
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss",
            "ru/sbsoft/svc/theme/neptune/client/base/panel/Css3Header.gss",
            "SlicedHeader.gss"})
    @Override
    SlicedHeaderStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("panel-background.png")
    ImageResource headerBackground();
  }

  public interface SlicedHeaderStyle extends Css3HeaderStyle {
  }

  public SlicedHeaderAppearance() {
    this(GWT.<SlicedHeaderResources>create(SlicedHeaderResources.class));
  }

  public SlicedHeaderAppearance(SlicedHeaderResources resources) {
    super(resources);
  }

}
