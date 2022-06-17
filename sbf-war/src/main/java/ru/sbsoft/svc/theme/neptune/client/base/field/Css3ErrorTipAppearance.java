/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.neptune.client.base.tips.Css3TipAppearance;
import ru.sbsoft.svc.widget.core.client.form.error.SideErrorHandler.SideErrorTooltipAppearance;

public class Css3ErrorTipAppearance extends Css3TipAppearance implements SideErrorTooltipAppearance {
  interface Css3ErrorTipResources extends Css3TipResources {
    @Source("exclamation.png")
    ImageResource errorIcon();

    @Override
    @Source("Css3ErrorTip.gss")
    Css3ErrorTipStyle style();
  }
  interface Css3ErrorTipStyle extends Css3TipStyle {

  }
  public Css3ErrorTipAppearance() {
    super(GWT.<Css3ErrorTipResources>create(Css3ErrorTipResources.class));
  }
}
