/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.toolbar;

import com.google.gwt.core.shared.GWT;
import ru.sbsoft.svc.theme.base.client.toolbar.LabelToolItemDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3LabelToolItemAppearance extends LabelToolItemDefaultAppearance {
  public interface Css3LabelToolItemResources extends LabelToolItemResources {
    @Override
    @Source("Css3LabelToolItem.gss")
    Css3LabelToolItemStyle css();

    ThemeDetails theme();
  }
  public interface Css3LabelToolItemStyle extends LabelToolItemStyle {

  }
  public Css3LabelToolItemAppearance() {
    super(GWT.<Css3LabelToolItemResources>create(Css3LabelToolItemResources.class));
  }
}
