/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.blue.client.window.BlueWindowAppearance;

public class BlueMessageBoxAppearance extends BlueWindowAppearance {

  public interface BlueMessageBoxResources extends BlueWindowResources, ClientBundle {

    @Source({
        "ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss",
        "ru/sbsoft/svc/theme/blue/client/window/BlueWindow.gss"})
    @Override
    BlueWindowStyle style();

  }

  public BlueMessageBoxAppearance() {
    super((BlueMessageBoxResources) GWT.create(BlueMessageBoxResources.class));
  }
}
