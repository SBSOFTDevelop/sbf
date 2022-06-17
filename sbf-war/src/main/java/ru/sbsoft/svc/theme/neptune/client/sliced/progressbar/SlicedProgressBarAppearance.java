/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.progressbar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.progressbar.Css3ProgressBarAppearance;

public class SlicedProgressBarAppearance extends Css3ProgressBarAppearance {
  public interface SlicedProgressBarResources extends Css3ProgressBarResources {
    @Source("progressbar-bar.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bar();

    @Source("progressbar-bg.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource innerBar();

    @Override
    @Source("SlicedProgressBar.gss")
    SlicedProgressBarStyle styles();

  }

  public interface SlicedProgressBarStyle extends Css3ProgressBarStyles {

  }

  public SlicedProgressBarAppearance() {
    this(GWT.<SlicedProgressBarResources>create(SlicedProgressBarResources.class));
  }

  public SlicedProgressBarAppearance(SlicedProgressBarResources resources) {
    super(resources);
  }
}
