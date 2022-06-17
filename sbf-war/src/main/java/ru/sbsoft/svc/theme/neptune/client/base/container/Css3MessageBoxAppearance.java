/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.container;

import com.google.gwt.core.shared.GWT;
import ru.sbsoft.svc.theme.base.client.container.MessageBoxDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3MessageBoxAppearance extends MessageBoxDefaultAppearance {
  public interface Css3MessageBoxResources extends MessageBoxResources {
    @Override
    @Source("Css3MessageBox.gss")
    Css3MessageBoxStyles style();

    ThemeDetails theme();
  }

  public interface Css3MessageBoxStyles extends MessageBoxBaseStyle {

  }

  public Css3MessageBoxAppearance() {
    this(GWT.<Css3MessageBoxResources>create(Css3MessageBoxResources.class));
  }

  public Css3MessageBoxAppearance(Css3MessageBoxResources resources) {
    super(resources);
  }
}
