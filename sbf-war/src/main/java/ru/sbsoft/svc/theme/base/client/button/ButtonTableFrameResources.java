/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.button;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame.TableFrameResources;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame.TableFrameStyle;

public interface ButtonTableFrameResources extends TableFrameResources, ClientBundle {
  
  @Source({"ru/sbsoft/svc/theme/base/client/frame/TableFrame.gss", "ru/sbsoft/svc/theme/base/client/button/ButtonTableFrame.gss"})
  @Override
  TableFrameStyle style();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource background();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource backgroundOverBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource backgroundPressedBorder();
  
  @Override
  ImageResource topLeftBorder();
  
  ImageResource topLeftOverBorder();
  
  ImageResource topLeftPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  @Override
  ImageResource topBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource topOverBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource topPressedBorder();
  
  @Override
  ImageResource topRightBorder();
  
  ImageResource topRightOverBorder();
  
  ImageResource topRightPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  @Override
  ImageResource leftBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource leftOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource leftPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  @Override
  ImageResource rightBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource rightOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Vertical)
  ImageResource rightPressedBorder();
  
  @Override
  ImageResource bottomLeftBorder();

  ImageResource bottomLeftOverBorder();

  ImageResource bottomLeftPressedBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  @Override
  ImageResource bottomBorder();
  
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource bottomOverBorder();

  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  ImageResource bottomPressedBorder();
  
  @Override
  ImageResource bottomRightBorder();

  ImageResource bottomRightOverBorder();

  ImageResource bottomRightPressedBorder();

}

