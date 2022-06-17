/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.window;

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
import ru.sbsoft.svc.theme.blue.client.panel.BlueFramedPanelAppearance.FramedPanelStyle;
import ru.sbsoft.svc.widget.core.client.Window.WindowAppearance;

public class BlueWindowAppearance extends FramedPanelBaseAppearance implements WindowAppearance {

  public interface BlueWindowDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface BlueWindowDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/frame/NestedDivFrame.gss", "BlueWindowDivFrame.gss"})
    @Override
    BlueWindowDivFrameStyle style();

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

  public interface BlueWindowStyle extends FramedPanelStyle {
    String ghost();
  }

  public interface BlueHeaderStyle extends HeaderStyle {

  }

  public interface BlueHeaderResources extends HeaderResources {
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "BlueWindowHeader.gss"})
    BlueHeaderStyle style();
  }

  public interface BlueWindowResources extends ContentPanelResources, ClientBundle {

    @Source({
        "ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss",
        "ru/sbsoft/svc/theme/base/client/window/Window.gss", "BlueWindow.gss"})
    @Override
    BlueWindowStyle style();

  }

  private BlueWindowStyle style;

  public BlueWindowAppearance() {
    this((BlueWindowResources) GWT.create(BlueWindowResources.class));
  }

  public BlueWindowAppearance(BlueWindowResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<BlueWindowDivFrameResources> create(BlueWindowDivFrameResources.class)));

    this.style = resources.style();
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new HeaderDefaultAppearance(GWT.<BlueHeaderResources> create(BlueHeaderResources.class));
  }

  @Override
  public String ghostClass() {
    return style.ghost();
  }
}
