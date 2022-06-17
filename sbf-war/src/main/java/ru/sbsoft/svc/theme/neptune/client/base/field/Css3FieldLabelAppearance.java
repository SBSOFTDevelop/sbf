/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.shared.GWT;
import ru.sbsoft.svc.theme.base.client.field.FieldLabelDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3FieldLabelAppearance extends FieldLabelDefaultAppearance {
  public interface Css3FieldLabelResources extends FieldLabelResources {
    @Override
    @Source("Css3FieldLabel.gss")
    Css3FieldLabelStyles css();

    ThemeDetails theme();
  }

  public interface Css3FieldLabelStyles extends FieldLabelDefaultAppearance.Style {

  }

  public Css3FieldLabelAppearance() {
    this(GWT.<Css3FieldLabelResources>create(Css3FieldLabelResources.class));
  }

  public Css3FieldLabelAppearance(Css3FieldLabelResources resources) {
    this(resources, GWT.<FieldLabelTemplate>create(FieldLabelTemplate.class));
  }

  public Css3FieldLabelAppearance(Css3FieldLabelResources resources, FieldLabelTemplate template) {
    super(resources, template);
  }
}
