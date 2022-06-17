/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3ContentPanelAppearance;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;

/**
 *
 */
public class SlicedContentPanelAppearance extends Css3ContentPanelAppearance {

  public interface SlicedContentPanelResources extends Css3ContentPanelResources {
    @Source("SlicedContentPanel.gss")
    @Override
    SlicedContentPanelStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("panel-background.png")
    ImageResource headerBackground();
  }

  public interface SlicedContentPanelStyle extends Css3ContentPanelStyle {
  }

  public SlicedContentPanelAppearance() {
    this(GWT.<SlicedContentPanelResources>create(SlicedContentPanelResources.class));
  }

  public SlicedContentPanelAppearance(SlicedContentPanelResources resources) {
    this(resources, GWT.<Css3ContentPanelTemplate>create(Css3ContentPanelTemplate.class));
  }

  public SlicedContentPanelAppearance(SlicedContentPanelResources resources, Css3ContentPanelTemplate template) {
    super(resources, template);
  }

  @Override
  public HeaderAppearance getHeaderAppearance() {
    return new Css3HeaderAppearance();
  }
}
