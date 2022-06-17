/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.gray.client.window.GrayWindowAppearance;

public class GrayMessageBoxAppearance extends GrayWindowAppearance {

  public interface GrayMessageBoxResources extends GrayWindowResources, ClientBundle {

    @Source({
        "ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss",
        "ru/sbsoft/svc/theme/gray/client/window/GrayWindow.gss"})
    @Override
    GrayWindowStyle style();

  }

  public GrayMessageBoxAppearance() {
    super((GrayMessageBoxResources) GWT.create(GrayMessageBoxResources.class));
  }
}
