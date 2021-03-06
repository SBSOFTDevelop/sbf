/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel.FieldLabelAppearance;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel.FieldLabelOptions;
import ru.sbsoft.svc.widget.core.client.form.FormPanel.LabelAlign;

public class FieldLabelDefaultAppearance implements FieldLabelAppearance {

  public interface FieldLabelResources extends ClientBundle {

    @Source("FieldLabel.gss")
    Style css();

  }

  public interface Style extends CssResource {

    String clearLeft();

    String fieldElement();

    String fieldItem();

    String fieldItemLabelTop();

    String fieldLabel();

  }

  public interface FieldLabelTemplate extends XTemplates {

    @XTemplate(source = "FieldLabel.html")
    SafeHtml render(String id, Style style, SafeStyles fieldLabelStyles, SafeStyles fieldElementStyles);

  }

  private FieldLabelTemplate template;
  private Style style;

  public FieldLabelDefaultAppearance() {
    this(GWT.<FieldLabelResources> create(FieldLabelResources.class),
        GWT.<FieldLabelTemplate> create(FieldLabelTemplate.class));
  }

  public FieldLabelDefaultAppearance(FieldLabelResources resources, FieldLabelTemplate template) {
    this.template = template;

    this.style = resources.css();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public void clearLabelFor(XElement parent) {
    getLabelElement(parent).removeAttribute("for");
  }

  @Override
  public XElement getChildElementWrapper(XElement parent) {
    // second child of parent
    XElement childElementWrapper = XElement.as(parent.getChild(1));
    assert childElementWrapper.is("div." + style.fieldElement());
    return childElementWrapper;
  }

  @Override
  public XElement getLabelElement(XElement parent) {
    XElement labelElement = XElement.as(parent.getFirstChildElement());
    assert labelElement.is("label." + style.fieldLabel());
    return labelElement;
  }

  @Override
  public void onUpdateOptions(XElement parent, FieldLabelOptions options) {
    LabelAlign labelAlign = options.getLabelAlign();
    XElement fieldElement = getChildElementWrapper(parent);
    XElement labelElement = getLabelElement(parent);

    // Adjust for label content, label separator
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    sb.append(options.getContent());
    sb.appendEscaped(options.getLabelSeparator());
    labelElement.setInnerSafeHtml(sb.toSafeHtml());

    // Adjust for label alignment
    if (labelAlign == LabelAlign.TOP) {
      parent.addClassName(style.fieldItemLabelTop());
    } else {
      parent.removeClassName(style.fieldItemLabelTop());
    }

    // Adjust for label width
    if (labelAlign == LabelAlign.TOP) {
      labelElement.getStyle().setProperty("width", "auto");
      fieldElement.getStyle().setPaddingLeft(0, Unit.PX);
    } else {
      int pad = options.getLabelPad();
      if (pad == 0) pad = 5;
      labelElement.getStyle().setWidth(options.getLabelWidth(), Unit.PX);
      fieldElement.getStyle().setPaddingLeft(options.getLabelWidth() + pad, Unit.PX);
    }

    // Adjust for label word wrap
    labelElement.getStyle().setProperty("whiteSpace", options.getWordWrap() ? "normal" : "nowrap");
  }

  @Override
  public void render(SafeHtmlBuilder sb, String id, FieldLabelOptions options) {
    int labelWidth = options.getLabelWidth();
    LabelAlign align = options.getLabelAlign();

    int pad = options.getLabelPad();
    if (pad == 0) pad = 5;

    String fieldLabelWidth = align == LabelAlign.TOP ? "auto" : (labelWidth + "px");

    SafeStylesBuilder fieldLabelStylesBuilder = new SafeStylesBuilder().appendTrustedString("width:" + fieldLabelWidth + ";");
    fieldLabelStylesBuilder.appendTrustedString("white-space: " + (options.getWordWrap() ? "normal" : "nowrap") + ";");
    
    SafeStyles fieldLabelStyles = fieldLabelStylesBuilder.toSafeStyles();

    String fieldElementPadding = align == LabelAlign.TOP ? "0" : (labelWidth + pad + "px");
    SafeStyles fieldElementStyles = SafeStylesUtils.fromTrustedString("padding-left:" + fieldElementPadding + ";");

    sb.append(template.render(id, style, fieldLabelStyles, fieldElementStyles));
  }

  @Override
  public void setLabelFor(XElement parent, String id) {
    getLabelElement(parent).setAttribute("for", id + "-input");
  }

}
