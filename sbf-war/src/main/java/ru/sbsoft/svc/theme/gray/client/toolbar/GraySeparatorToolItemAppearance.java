/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.toolbar.SeparatorToolItemDefaultAppearance;

public class GraySeparatorToolItemAppearance extends SeparatorToolItemDefaultAppearance {

  public interface GraySeparatorToolItemResources extends SeparatorToolItemResources {
    @Override
    @Source(value = "separatorBackground.gif")
    public ImageResource background();
  }
  
  public GraySeparatorToolItemAppearance() {
    this(GWT.<GraySeparatorToolItemResources>create(GraySeparatorToolItemResources.class), GWT.<Template>create(Template.class));
  }
  
  public GraySeparatorToolItemAppearance(GraySeparatorToolItemResources resources, Template template) {
    super(resources, template);
  }

}
