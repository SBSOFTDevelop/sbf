/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.field.HtmlEditorDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;


public class Css3HtmlEditorAppearance extends HtmlEditorDefaultAppearance {

  public interface Css3HtmlEditorResources extends HtmlEditorResources {

    @Source({"ru/sbsoft/svc/theme/base/client/field/HtmlEditor.gss", "Css3HtmlEditor.gss"})
    @Override
    Css3HtmlEditorStyle css();

    ThemeDetails theme();

    ImageResource bold();

    ImageResource fontColor();

    ImageResource fontDecrease();

    ImageResource fontHighlight();

    ImageResource fontIncrease();

    ImageResource italic();

    ImageResource justifyCenter();

    ImageResource justifyLeft();

    ImageResource justifyRight();

    ImageResource link();

    ImageResource ol();

    ImageResource source();

    ImageResource ul();

    ImageResource underline();
  }

  public interface Css3HtmlEditorStyle extends HtmlEditorStyle {
  }

  public Css3HtmlEditorAppearance() {
    this(GWT.<Css3HtmlEditorResources>create(Css3HtmlEditorResources.class));
  }

  public Css3HtmlEditorAppearance(Css3HtmlEditorResources resources) {
    super(resources);
  }
}
