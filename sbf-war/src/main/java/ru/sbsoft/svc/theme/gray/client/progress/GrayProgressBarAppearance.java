/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.progress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.progress.ProgressBarDefaultAppearance;

public class GrayProgressBarAppearance extends ProgressBarDefaultAppearance {

  public interface GrayProgressBarResources extends ProgressBarResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/progress/ProgressBar.gss", "ProgressBar.gss"})
    @Override
    ProgressBarStyle style();

    @Source("progress-bg.gif")
    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bar();
    
    @Source("bg.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource innerBar();
  }

  public GrayProgressBarAppearance() {
    super(GWT.<ProgressBarResources> create(GrayProgressBarResources.class),
        GWT.<ProgressBarTemplate> create(ProgressBarTemplate.class));
  }

}
