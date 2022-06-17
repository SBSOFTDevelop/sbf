/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import ru.sbsoft.svc.theme.base.client.panel.FramedPanelBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class GrayFramedPanelAppearance extends FramedPanelBaseAppearance {

  public interface FramedPanelStyle extends ContentPanelStyle {

  }

  public interface GrayFramePanelResources extends ContentPanelResources {
    @Source({"ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss", "GrayFramedPanel.gss"})
    @Override
    FramedPanelStyle style();
  }

  public interface GrayFramePanelNestedDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface GrayFramedPanelDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/frame/NestedDivFrame.gss", "GrayFramedPanelDivFrame.gss"})
    @Override
    GrayFramePanelNestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Override
    ImageResource topLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource bottomBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomRightBorder();

  }

  public GrayFramedPanelAppearance() {
    this(GWT.<GrayFramePanelResources> create(GrayFramePanelResources.class));
  }

  public GrayFramedPanelAppearance(GrayFramePanelResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<FramedPanelDivFrameResources> create(GrayFramedPanelDivFrameResources.class)));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new GrayHeaderFramedAppearance();
  }

}
