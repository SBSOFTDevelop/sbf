/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.button;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.button.ButtonGroupBaseAppearance.ButtonGroupTableFrameStyle;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame.TableFrameResources;

public interface ButtonGroupBaseTableFrameResources extends TableFrameResources, ClientBundle {

  @Source({"ru/sbsoft/svc/theme/base/client/frame/TableFrame.gss", "ButtonGroupTableFrame.gss"})
  @Override
  ButtonGroupTableFrameStyle style();

  @Source("ru/sbsoft/svc/core/public/clear.gif")
  @ImageOptions(repeatStyle = RepeatStyle.Both)
  ImageResource background();

  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource backgroundOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource backgroundPressedBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource topOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource topPressedBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource leftOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource leftPressedBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Both, preventInlining = true)
  @Override
  ImageResource bottomLeftBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Both, preventInlining = true)
  @Override
  ImageResource bottomRightBorder();

}
