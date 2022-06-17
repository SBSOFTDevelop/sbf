/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.button.IconButtonDefaultAppearance.IconButtonStyle;
import ru.sbsoft.svc.theme.neptune.client.base.field.Css3DualListFieldAppearance;

/**
 *
 */
public class SlicedDualListFieldAppearance extends Css3DualListFieldAppearance {
  public interface SlicedDualListFieldResources extends Css3DualListFieldResources {
    @Override
    @Source("SlicedDualListField.gss")
    @Import(IconButtonStyle.class)
    SlicedDualListFieldStyle style();

    @Source("dualListField-button-background.png")
    ImageResource background();
  }

  public interface SlicedDualListFieldStyle extends Css3DualListFieldStyle {

  }


  public SlicedDualListFieldAppearance() {
    this(GWT.<SlicedDualListFieldResources>create(SlicedDualListFieldResources.class));
  }

  public SlicedDualListFieldAppearance(SlicedDualListFieldResources resources) {
    super(resources);
  }
}
