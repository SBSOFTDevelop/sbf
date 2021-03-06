/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import ru.sbsoft.svc.theme.base.client.panel.FramedPanelBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance.HeaderResources;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance.HeaderStyle;
import ru.sbsoft.svc.theme.gray.client.panel.GrayFramedPanelAppearance.FramedPanelStyle;
import ru.sbsoft.svc.widget.core.client.Window.WindowAppearance;

public class GrayWindowAppearance extends FramedPanelBaseAppearance implements WindowAppearance {

  public interface GrayWindowDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface GrayWindowDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/frame/NestedDivFrame.gss", "GrayWindowDivFrame.gss"})
    @Override
    GrayWindowDivFrameStyle style();

    @Source("ru/sbsoft/svc/core/public/clear.gif")
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

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomRightBorder();

  }

  public interface GrayWindowStyle extends FramedPanelStyle {
    String ghost();
  }

  public interface GrayHeaderStyle extends HeaderStyle {

  }

  public interface GrayHeaderResources extends HeaderResources {
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "GrayWindowHeader.gss"})
    GrayHeaderStyle style();
  }

  public interface GrayWindowResources extends ContentPanelResources, ClientBundle {

    @Source({
        "ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss",
        "ru/sbsoft/svc/theme/base/client/window/Window.gss", "GrayWindow.gss"})
    @Override
    GrayWindowStyle style();

  }

  private GrayWindowStyle style;

  public GrayWindowAppearance() {
    this((GrayWindowResources) GWT.create(GrayWindowResources.class));
  }

  public GrayWindowAppearance(GrayWindowResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<GrayWindowDivFrameResources> create(GrayWindowDivFrameResources.class)));

    this.style = resources.style();
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new HeaderDefaultAppearance(GWT.<GrayHeaderResources> create(GrayHeaderResources.class));
  }

  @Override
  public String ghostClass() {
    return style.ghost();
  }
}
