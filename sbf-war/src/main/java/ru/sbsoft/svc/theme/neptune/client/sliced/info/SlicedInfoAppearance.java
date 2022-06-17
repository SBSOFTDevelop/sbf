/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.info;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.info.InfoDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class SlicedInfoAppearance extends InfoDefaultAppearance {
  interface SlicedInfoResources extends InfoResources {
    @Source({"ru/sbsoft/svc/theme/base/client/frame/DivFrame.gss", "Info.gss"})
    @Override
    SlicedInfoStyle style();

    @Source("background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("corner-bc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bottomBorder();

    @Source("corner-bl.png")
    ImageResource bottomLeftBorder();

    @Source("corner-br.png")
    ImageResource bottomRightBorder();

    @Source("side-l.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource leftBorder();

    @Source("side-r.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource rightBorder();

    @Source("corner-tc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource topBorder();

    @Source("corner-tl.png")
    ImageResource topLeftBorder();

    @Source("corner-tr.png")
    ImageResource topRightBorder();

    ThemeDetails theme();
  }

  interface SlicedInfoStyle extends InfoStyle {

  }

  public SlicedInfoAppearance() {
    this(GWT.<SlicedInfoResources>create(SlicedInfoResources.class));
  }

  public SlicedInfoAppearance(SlicedInfoResources resources) {
    this(resources, GWT.<Template>create(Template.class));
  }

  public SlicedInfoAppearance(SlicedInfoResources resources, Template template) {
    super(template, resources);
  }
}
