/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutContainerAppearance;


public class HBoxLayoutDefaultAppearance extends BoxLayoutDefaultAppearance implements HBoxLayoutContainerAppearance {

  public interface HBoxLayoutBaseResources extends BoxLayoutBaseResources {
    @Override
    @Source({"BoxLayout.gss", "HBoxLayout.gss"})
    HBoxLayoutStyle style();

    @Source("more.gif")
    ImageResource moreIcon();
  }

  public interface HBoxLayoutStyle extends BoxLayoutStyle {
    String moreIcon();
  }

  private HBoxLayoutBaseResources resources;
  private HBoxLayoutStyle style;

  public HBoxLayoutDefaultAppearance() {
    this(GWT.<HBoxLayoutBaseResources>create(HBoxLayoutBaseResources.class));
  }

  public HBoxLayoutDefaultAppearance(HBoxLayoutBaseResources resources) {
    super(resources);
    this.resources = resources;
    this.style = resources.style();
  }

  @Override
  public ImageResource moreIcon() {
    return resources.moreIcon();
  }

  @Override
  public String moreButtonStyle() {
    return style.moreIcon();
  }
}
