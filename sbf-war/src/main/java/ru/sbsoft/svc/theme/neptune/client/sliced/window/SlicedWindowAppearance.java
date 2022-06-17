/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.window;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.sliced.panel.SlicedFramedPanelAppearance;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;
import ru.sbsoft.svc.widget.core.client.Window.WindowAppearance;

public class SlicedWindowAppearance extends SlicedFramedPanelAppearance implements WindowAppearance {

  public interface SlicedWindowResources extends SlicedFramePanelResources {
    @Override
    @Source("SlicedWindow.gss")
    SlicedWindowStyle style();
  }

  public interface SlicedWindowStyle extends FramedPanelStyle {
    String ghost();
  }

  public interface SlicedWindowDivFrameResources extends SlicedFramedPanelDivFrameResources {
    @Source({"ru/sbsoft/svc/theme/neptune/client/sliced/panel/SlicedFramedPanelDivFrame.gss"})
    @Override
    SlicedWindowNestedDivFrameStyle style();

    @Override
    @Source("window-background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("window-tl.png")
    @Override
    ImageResource topLeftBorder();

    @Source("window-tc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Source("window-tr.png")
    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

    @Source("window-l.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @Source("window-r.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @Source("window-bl.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomLeftBorder();

    @Source("window-bc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource bottomBorder();

    @Source("window-br.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomRightBorder();
  }

  public interface SlicedWindowNestedDivFrameStyle extends SlicedFramePanelNestedDivFrameStyle {

  }

  private final SlicedWindowResources res;

  public SlicedWindowAppearance() {
    this(GWT.<SlicedWindowResources>create(SlicedWindowResources.class));

  }

  public SlicedWindowAppearance(SlicedWindowResources slicedWindowResources) {
    this(slicedWindowResources, GWT.<SlicedWindowDivFrameResources>create(SlicedWindowDivFrameResources.class));
  }

  public SlicedWindowAppearance(SlicedWindowResources slicedWindowResources, SlicedWindowDivFrameResources frameResources) {
    super(slicedWindowResources, frameResources);
    this.res = slicedWindowResources;
  }

  @Override
  public HeaderAppearance getHeaderAppearance() {
    return new SlicedWindowHeaderAppearance();
  }

  @Override
  public String ghostClass() {
    return res.style().ghost();
  }
}
