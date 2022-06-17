/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.neptune.client.base.toolbar.Css3SeparatorToolItemAppearance;

/**
 *
 */
public class SlicedSeparatorToolItemAppearance extends Css3SeparatorToolItemAppearance {

  public interface SlicedSeparatorToolItemResources extends Css3SeparatorToolItemResources {

    @Source({"ru/sbsoft/svc/theme/neptune/client/base/toolbar/Css3SeparatorToolItem.gss", "SlicedSeparatorToolItem.gss"})
    @Override
    Css3SeparatorToolItemStyle style();

    ImageResource separator();
  }

  public SlicedSeparatorToolItemAppearance() {
    this(GWT.<SlicedSeparatorToolItemResources>create(SlicedSeparatorToolItemResources.class));
  }

  public SlicedSeparatorToolItemAppearance(SlicedSeparatorToolItemResources resources) {
    super(resources);
  }
}
