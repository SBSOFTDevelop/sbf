/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.fieldset;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.field.FieldSetDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.button.IconButton;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;

public class Css3FieldSetAppearance extends FieldSetDefaultAppearance {
  public interface Css3FieldSetResources extends FieldSetResources {
    @Source({"ru/sbsoft/svc/theme/base/client/field/FieldSet.gss", "Css3FieldSet.gss"})
    @Override
    Css3FieldSetStyle css();

    ThemeDetails theme();

    ImageResource collapseIcon();

    ImageResource collapseOverIcon();

    ImageResource expandIcon();

    ImageResource expandOverIcon();
  }

  public interface Css3FieldSetStyle extends FieldSetStyle {
    String collapseIcon();

    String collapseOverIcon();

    String expandIcon();

    String expandOverIcon();
  }

  private Css3FieldSetResources resources;

  public Css3FieldSetAppearance() {
    this(GWT.<Css3FieldSetResources>create(Css3FieldSetResources.class));
  }

  public Css3FieldSetAppearance(Css3FieldSetResources resources) {
    super(resources);
    this.resources = resources;
  }

  @Override
  public IconConfig collapseIcon() {
    return new IconConfig(resources.css().collapseIcon(), resources.css().collapseOverIcon());
  }

  @Override
  public IconButton.IconConfig expandIcon() {
    return new IconConfig(resources.css().expandIcon(), resources.css().expandOverIcon());
  }
}
