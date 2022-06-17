/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.theme.base.client.toolbar.ToolBarBaseAppearance;

public class GrayToolBarAppearance extends ToolBarBaseAppearance {

  public interface GrayToolBarResources extends ClientBundle {
    @Source({"ru/sbsoft/svc/theme/base/client/toolbar/ToolBarBase.gss", "GrayToolBar.gss"})
    GrayToolBarStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource background();

  }

  public interface GrayToolBarStyle extends ToolBarBaseStyle, CssResource {

  }

  private final GrayToolBarStyle style;
  private final GrayToolBarResources resources;

  public GrayToolBarAppearance() {
    this(GWT.<GrayToolBarResources> create(GrayToolBarResources.class));
  }

  public GrayToolBarAppearance(GrayToolBarResources resources) {
    this.resources = resources;
    this.style = this.resources.style();
   
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public String toolBarClassName() {
    return style.toolBar();
  }

}
