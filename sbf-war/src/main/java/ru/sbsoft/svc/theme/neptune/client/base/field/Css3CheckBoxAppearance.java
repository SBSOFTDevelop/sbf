/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.cell.core.client.form.CheckBoxCell;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;

public class Css3CheckBoxAppearance extends Css3ValueBaseFieldAppearance implements CheckBoxCell.CheckBoxAppearance {

  public interface Css3CheckBoxResources extends Css3ValueBaseFieldResources, ClientBundle {
    @Source({"Css3ValueBaseField.gss", "Css3CheckBox.gss"})
    Css3CheckBoxStyle style();

    ImageResource checked();

    ImageResource unchecked();
  }

  public interface Css3CheckBoxStyle extends Css3ValueBaseFieldStyle {
    String checkBoxLabel();
  }

  protected final Css3CheckBoxResources resources;
  protected final Css3CheckBoxStyle style;
  protected String type = "checkbox";

  public Css3CheckBoxAppearance() {
    this(GWT.<Css3CheckBoxResources> create(Css3CheckBoxResources.class));
  }

  public Css3CheckBoxAppearance(Css3CheckBoxResources resources) {
    super(resources);
    this.resources = resources;
    this.style = resources.style();
  }

  @Override
  public void render(SafeHtmlBuilder sb, Boolean value, CheckBoxCell.CheckBoxCellOptions options) {
    String checkBoxId = XDOM.getUniqueId();

    String nameParam = options.getName() != null ? " name='" + options.getName() + "' " : "";
    String disabledParam = options.isDisabled() ? " disabled=true" : "";
    String readOnlyParam = options.isReadonly() ? " readonly" : "";
    String idParam = " id=" + checkBoxId;
    String typeParam = " type=" + type;
    String checkedParam = value ? " checked" : "";

    sb.appendHtmlConstant("<div class=" + style.wrap() + ">");
    sb.appendHtmlConstant("<input " + typeParam + nameParam + disabledParam + readOnlyParam + idParam + checkedParam + " />");
    sb.appendHtmlConstant("<label for=" + checkBoxId + " class=" + style.checkBoxLabel() + ">");
    if (options.getBoxLabel() != null) {
      sb.append(options.getBoxLabel());
    }
    sb.appendHtmlConstant("</label></div>");
  }

  @Override
  public void setBoxLabel(SafeHtml boxLabel, XElement parent) {
    parent.selectNode("." + resources.style().checkBoxLabel()).<LabelElement> cast().setInnerSafeHtml(boxLabel);
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.<XElement> cast().selectNode("input");
  }

  @Override
  public void setReadOnly(Element parent, boolean readOnly) {
    getInputElement(parent).<InputElement> cast().setReadOnly(readOnly);
  }

  @Override
  public void onEmpty(Element parent, boolean empty) {

  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    // Override method to prevent outline from being applied to check boxes on
    // focus
  }

  @Override
  public void onValid(Element parent, boolean valid) {
    // no-op, cb is true or false...
  }
}
