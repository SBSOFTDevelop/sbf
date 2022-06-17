/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.resizable;


import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.resizable.ResizableBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3ResizableAppearance extends ResizableBaseAppearance {
  public interface Css3ResizableResources extends ResizableResources {
    @Source({"ru/sbsoft/svc/theme/base/client/resizable/Resizable.gss", "Css3Resizable.gss"})
    Css3ResizableStyle style();

    ThemeDetails theme();
  }

  public interface Css3ResizableStyle extends ResizableStyle {
  }

  public Css3ResizableAppearance() {
    this(GWT.<Css3ResizableResources>create(Css3ResizableResources.class));
  }

  public Css3ResizableAppearance(Css3ResizableResources resources) {
    super(resources);
  }
}
