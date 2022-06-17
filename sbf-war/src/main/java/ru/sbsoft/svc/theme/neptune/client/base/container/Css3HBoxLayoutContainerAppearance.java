/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.container.HBoxLayoutDefaultAppearance;

public class Css3HBoxLayoutContainerAppearance extends HBoxLayoutDefaultAppearance {

  public interface Css3HBoxLayoutContainerResources extends HBoxLayoutBaseResources {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/container/BoxLayout.gss", "Css3HBoxLayoutContainer.gss"})
    HBoxLayoutStyle style();

    @Override
    ImageResource moreIcon();

    ImageResource moreIconToolBar();
  }

  public interface Css3HBoxLayoutContainerStyle extends HBoxLayoutStyle {

  }

  public Css3HBoxLayoutContainerAppearance() {
    this(GWT.<Css3HBoxLayoutContainerResources>create(Css3HBoxLayoutContainerResources.class));
  }

  public Css3HBoxLayoutContainerAppearance(Css3HBoxLayoutContainerResources resources) {
    super(resources);
  }
}
