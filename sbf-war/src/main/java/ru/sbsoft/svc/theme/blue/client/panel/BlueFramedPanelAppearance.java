/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import ru.sbsoft.svc.theme.base.client.panel.FramedPanelBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class BlueFramedPanelAppearance extends FramedPanelBaseAppearance {

  public interface FramedPanelStyle extends ContentPanelStyle {

  }

  public interface BlueFramePanelResources extends ContentPanelResources {
    @Source({"ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss", "BlueFramedPanel.gss"})
    @Override
    FramedPanelStyle style();
  }

  public interface BlueFramePanelNestedDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface BlueFramedPanelDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/frame/NestedDivFrame.gss", "BlueFramedPanelDivFrame.gss"})
    @Override
    BlueFramePanelNestedDivFrameStyle style();

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

  public BlueFramedPanelAppearance() {
    this(GWT.<BlueFramePanelResources> create(BlueFramePanelResources.class));
  }

  public BlueFramedPanelAppearance(BlueFramePanelResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<FramedPanelDivFrameResources> create(BlueFramedPanelDivFrameResources.class)));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new BlueHeaderFramedAppearance();
  }

}
