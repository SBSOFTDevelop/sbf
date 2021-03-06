/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import ru.sbsoft.svc.cell.core.client.form.TextAreaInputCell.TextAreaAppearance;
import ru.sbsoft.svc.cell.core.client.form.TextAreaInputCell.TextAreaCellOptions;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Size;
import ru.sbsoft.svc.theme.base.client.field.TextFieldDefaultAppearance.TextFieldStyle;

public class TextAreaDefaultAppearance extends ValueBaseFieldDefaultAppearance implements TextAreaAppearance {

  public interface TextAreaResources extends ValueBaseFieldResources, ClientBundle {

    @Source({"ValueBaseField.gss", "TextField.gss", "TextArea.gss"})
    TextAreaStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource textBackground();

  }

  public interface TextAreaStyle extends TextFieldStyle {

  }

  private final TextAreaResources res;
  private final TextAreaStyle style;

  public TextAreaDefaultAppearance() {
    this(GWT.<TextAreaResources> create(TextAreaResources.class));
  }

  public TextAreaDefaultAppearance(TextAreaResources resources) {
    super(resources);
    this.res = resources;
    this.style = this.res.css();
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.getFirstChildElement().getFirstChildElement().cast();// textarea
  }

  @Override
  public void onResize(XElement parent, int width, int height) {
    Element div = parent.getFirstChildElement();

    Size adj = adjustTextAreaSize(width, height);

    if (width != -1) {
      div.getStyle().setWidth(width, Unit.PX);
      width = adj.getWidth();
      div.getFirstChildElement().getStyle().setWidth(width, Unit.PX);
    }

    if (height != -1) {
      height = adj.getHeight();
      if (height != -1) {
        div.getFirstChildElement().getStyle().setHeight(height, Unit.PX);
      }
    }
  }

  @Override
  public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
    int width = options.getWidth();
    int height = options.getHeight();

    boolean empty = false;

    String name = options.getName() != null ? " name='" + options.getName() + "' " : "";
    String disabled = options.isDisabled() ? " disabled=true" : "";
    String ro = options.isReadonly() ? " readonly" : "";
    String placeholder = options.getEmptyText() != null ? " placeholder='" + SafeHtmlUtils.htmlEscape(options.getEmptyText()) + "' " : "";

    if ((value == null || value.equals("")) && options.getEmptyText() != null) {
      if (SVC.isIE8() || SVC.isIE9()) {
        value = options.getEmptyText();
      }
      empty = true;
    }

    if (width == -1) {
      width = 150;
    }

    String inputStyles = "";
    String wrapStyles = "";

    Size adjusted = adjustTextAreaSize(width, height);

    if (width != -1) {
      wrapStyles += "width:" + width + "px;";
      width = adjusted.getWidth();
      inputStyles += "width:" + width + "px;";
    }

    if (height != -1) {
      height = adjusted.getHeight();
      inputStyles += "height: " + height + "px;";
    }

    String cls = style.area() + " " + style.field();
    if (empty) {
      cls += " " + style.empty();
    }

    if (options instanceof TextAreaCellOptions) {
      TextAreaCellOptions opts = (TextAreaCellOptions) options;
      inputStyles += "resize:" + opts.getResizable().name().toLowerCase() + ";";
    }

    sb.appendHtmlConstant("<div style='" + wrapStyles + "' class='" + style.wrap() + "'>");
    sb.appendHtmlConstant("<textarea " + name + disabled + " style='" + inputStyles + "' type='text' class='" + cls
        + "'" + ro + placeholder + ">");
    sb.append(SafeHtmlUtils.fromString(value));
    sb.appendHtmlConstant("</textarea></div>");
  }

  protected Size adjustTextAreaSize(int width, int height) {
    if (width != -1) {
      // 2px border
      width -= 2;
      
      // 6px margin except for gecko which has 0px margin
      if (!SVC.isGecko()) {
        width -= 6;
      }
    }

    if (height != -1) {
      // 2px border
      height -= 2;

      // 2px margin except gecko which has 0px margin
      if (!SVC.isGecko()) {
        height -= 2;
      }
    }

    return new Size(width, height);
  }

}
