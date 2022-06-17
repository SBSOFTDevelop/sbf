/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.resizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.resizable.ResizableBaseAppearance;

public class BlueResizableAppearance extends ResizableBaseAppearance {

  public interface BlueResizableResources extends ResizableResources, ClientBundle {

    ImageResource handleEast();

    ImageResource handleNortheast();

    ImageResource handleNorthwest();

    ImageResource handleSouth();

    ImageResource handleSoutheast();

    ImageResource handleSouthwest();

    @Source({"ru/sbsoft/svc/theme/base/client/resizable/Resizable.gss", "BlueResizable.gss"})
    BlueResizableStyle style();

  }

  public interface BlueResizableStyle extends ResizableStyle {
  }

  public BlueResizableAppearance() {
    this(GWT.<BlueResizableResources> create(BlueResizableResources.class));
  }

  public BlueResizableAppearance(ResizableResources resources) {
    super(resources);
  }

}
