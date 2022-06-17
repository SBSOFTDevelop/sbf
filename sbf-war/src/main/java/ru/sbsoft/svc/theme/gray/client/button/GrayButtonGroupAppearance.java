/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.button.ButtonGroupBaseAppearance;
import ru.sbsoft.svc.theme.base.client.button.ButtonGroupBaseTableFrameResources;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame;

public class GrayButtonGroupAppearance extends ButtonGroupBaseAppearance {

  public interface GrayButtonGroupTableFrameResources extends ButtonGroupBaseTableFrameResources {

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

  public interface GrayButtonGroupResources extends ButtonGroupResources {
    @Source({"ru/sbsoft/svc/theme/base/client/button/ButtonGroup.gss", "GrayButtonGroup.gss"})
    ButtonGroupStyle css();
  }

  public GrayButtonGroupAppearance() {
    this(GWT.<GrayButtonGroupResources> create(GrayButtonGroupResources.class));
  }

  public GrayButtonGroupAppearance(GrayButtonGroupResources resources) {
    super(resources, GWT.<GroupTemplate> create(GroupTemplate.class), new TableFrame(
        GWT.<GrayButtonGroupTableFrameResources> create(GrayButtonGroupTableFrameResources.class)));
  }

}
