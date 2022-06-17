/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.resources.client.CssResource;
import ru.sbsoft.svc.cell.core.client.form.ValueBaseInputCell.ValueBaseFieldAppearance;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.form.Field.FieldStyles;

public abstract class Css3ValueBaseFieldAppearance implements ValueBaseFieldAppearance {

  public interface Css3ValueBaseFieldResources {
    Css3ValueBaseFieldStyle style();

    ThemeDetails theme();
  }

  public interface Css3ValueBaseFieldStyle extends CssResource, FieldStyles {
    @Override
    String focus();

    @Override
    String invalid();

    String empty();

    String field();

    String readonly();

    String wrap();

  }

  private final Css3ValueBaseFieldStyle style;

  public Css3ValueBaseFieldAppearance(Css3ValueBaseFieldResources resources) {
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public void onEmpty(Element parent, boolean empty) {
    getInputElement(parent).setClassName(style.empty(), empty);
  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    parent.<XElement>cast().setClassName(style.focus(), focus);
  }

  @Override
  public void onValid(Element parent, boolean valid) {
    parent.<XElement>cast().setClassName(style.invalid(), !valid);
  }

  @Override
  public void setReadOnly(Element parent, boolean readOnly) {
    getInputElement(parent).<InputElement>cast().setReadOnly(readOnly);
    getInputElement(parent).setClassName(style.readonly(), readOnly);
  }
}
