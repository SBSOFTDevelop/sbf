/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.cell.core.client.form.CheckBoxCell.CheckBoxAppearance;
import ru.sbsoft.svc.cell.core.client.form.CheckBoxCell.CheckBoxCellOptions;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;

public class CheckBoxDefaultAppearance extends ValueBaseFieldDefaultAppearance implements CheckBoxAppearance {

  public interface CheckBoxResources extends ValueBaseFieldResources, ClientBundle {
    @Source({"ValueBaseField.gss", "CheckBox.gss"})
    CheckBoxStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource textBackground();
  }

  public interface CheckBoxStyle extends ValueBaseFieldStyle {

    String checkBoxLabel();

  }

  protected final CheckBoxResources resources;
  protected final CheckBoxStyle style;
  protected String type = "checkbox";

  public CheckBoxDefaultAppearance() {
    this(GWT.<CheckBoxResources> create(CheckBoxResources.class));
  }

  public CheckBoxDefaultAppearance(CheckBoxResources resources) {
    super(resources);
    this.resources = resources;
    this.style = resources.css();
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.<XElement> cast().selectNode("input");
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

  @Override
  public void render(SafeHtmlBuilder sb, Boolean value, CheckBoxCellOptions options) {
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
    sb.append(options.getBoxLabel());
    sb.appendHtmlConstant("</label></div>");

  }

  @Override
  public void setBoxLabel(SafeHtml boxLabel, XElement parent) {
    parent.selectNode("." + resources.css().checkBoxLabel()).<LabelElement> cast().setInnerSafeHtml(boxLabel);
  }

  @Override
  public void setReadOnly(Element parent, boolean readOnly) {
    getInputElement(parent).<InputElement> cast().setReadOnly(readOnly);
  }

}
