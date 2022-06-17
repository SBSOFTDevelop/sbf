/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.button.ButtonGroupBaseAppearance;
import ru.sbsoft.svc.theme.base.client.button.ButtonGroupBaseTableFrameResources;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame;

public class BlueButtonGroupAppearance extends ButtonGroupBaseAppearance {

  public interface BlueButtonGroupTableFrameResources extends ButtonGroupBaseTableFrameResources {

    @Source({
        "ru/sbsoft/svc/theme/base/client/frame/TableFrame.gss",
        "ru/sbsoft/svc/theme/base/client/button/ButtonGroupTableFrame.gss"})
    @Override
    ButtonGroupTableFrameStyle style();

    @Source("ru/sbsoft/svc/core/public/clear.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("groupTopLeftBorder.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource topLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("groupTopBorder.gif")
    @Override
    ImageResource topBorder();

    @Override
    @Source("groupTopRightBorder.gif")
    ImageResource topRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Source("groupLeftBorder.gif")
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Source("groupRightBorder.gif")
    @Override
    ImageResource rightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("groupBottomBorder.gif")
    @Override
    ImageResource bottomBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("groupTopNoHeadBorder.gif")
    ImageResource topNoHeadBorder();
  }
  
  public BlueButtonGroupAppearance() {
    this(GWT.<ButtonGroupResources>create(ButtonGroupResources.class));
  }

  public BlueButtonGroupAppearance(ButtonGroupResources resources) {
    super(resources, GWT.<GroupTemplate> create(GroupTemplate.class), new TableFrame(
        GWT.<BlueButtonGroupTableFrameResources> create(BlueButtonGroupTableFrameResources.class)));
  }

}
