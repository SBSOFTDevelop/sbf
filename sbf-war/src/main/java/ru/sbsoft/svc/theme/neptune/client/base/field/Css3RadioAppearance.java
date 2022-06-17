/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.cell.core.client.form.RadioCell;

public class Css3RadioAppearance extends Css3CheckBoxAppearance implements RadioCell.RadioAppearance {

  public interface Css3RadioResources extends Css3CheckBoxResources {
    @Source({"Css3ValueBaseField.gss", "Css3CheckBox.gss", "Css3Radio.gss"})
    Css3RadioStyle style();

    ImageResource radioSelected();

    ImageResource radioUnselected();
  }

  public interface Css3RadioStyle extends Css3CheckBoxStyle {
  }

  public Css3RadioAppearance() {
    this(GWT.<Css3RadioResources> create(Css3RadioResources.class));
  }

  public Css3RadioAppearance(Css3CheckBoxResources resources) {
    super(resources);
    type = "radio";
  }
}
