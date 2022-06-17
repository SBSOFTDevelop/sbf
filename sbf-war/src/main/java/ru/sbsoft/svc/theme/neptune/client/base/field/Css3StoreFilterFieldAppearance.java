/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.widget.core.client.form.StoreFilterField.StoreFilterFieldAppearance;

/**
 *
 */
public class Css3StoreFilterFieldAppearance extends Css3TriggerFieldAppearance implements StoreFilterFieldAppearance {
  public interface Css3StoreFilterFieldResources extends Css3TriggerFieldResources {
    @Override
    @Source({"Css3ValueBaseField.gss", "Css3TextField.gss", "Css3TriggerField.gss", "Css3StoreFilterField.gss"})
    Css3StoreFilterFieldStyle style();

    @Override
    @Source("clearTrigger.png")
    ImageResource triggerArrow();

    @Override
    @Source("clearTriggerOver.png")
    ImageResource triggerArrowOver();

    @Override
    @Source("clearTriggerClick.png")
    ImageResource triggerArrowClick();
  }

  public interface Css3StoreFilterFieldStyle extends Css3TriggerFieldStyle {

  }

  public Css3StoreFilterFieldAppearance() {
    this(GWT.<Css3StoreFilterFieldResources>create(Css3StoreFilterFieldResources.class));
  }

  public Css3StoreFilterFieldAppearance(Css3StoreFilterFieldResources resources) {
    super(resources);
  }
}
