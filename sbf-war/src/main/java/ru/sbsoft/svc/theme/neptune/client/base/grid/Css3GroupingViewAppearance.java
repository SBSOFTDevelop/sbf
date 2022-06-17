/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.grid;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.grid.GroupingViewDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStateStyles;

public class Css3GroupingViewAppearance extends GroupingViewDefaultAppearance {

  public interface Css3GroupingViewResources extends GroupingViewResources {

    @Override
    ImageResource groupBy();

    ImageResource expand();

    ImageResource collapse();

    @Override
    @Import(GridStateStyles.class)
    @Source("Css3GroupingView.gss")
    Css3GroupingViewStyle style();

    ThemeDetails theme();
  }

  public interface Css3GroupingViewStyle extends GroupingViewStyle {

  }

  public Css3GroupingViewAppearance() {
    this(GWT.<Css3GroupingViewResources>create(Css3GroupingViewResources.class));
  }

  public Css3GroupingViewAppearance(Css3GroupingViewResources resources) {
    super(resources);
  }
}
