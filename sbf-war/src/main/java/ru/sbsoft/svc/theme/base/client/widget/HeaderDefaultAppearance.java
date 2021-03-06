/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.IconHelper;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;

public class HeaderDefaultAppearance implements HeaderAppearance {

  public interface HeaderStyle extends CssResource {
    String header();

    String headerBar();

    String headerHasIcon();

    String headerIcon();

    String headerText();
  }

  public interface HeaderResources extends ClientBundle {

    @Source("Header.gss")
    HeaderStyle style();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "Header.html")
    SafeHtml render(HeaderStyle style);
  }

  private final HeaderResources resources;
  private Template template;
  private final HeaderStyle style;

  public HeaderDefaultAppearance() {
    this(GWT.<HeaderResources> create(HeaderResources.class), GWT.<Template> create(Template.class));
  }

  public HeaderDefaultAppearance(HeaderResources resources) {
    this(resources, GWT.<Template> create(Template.class));
  }

  public HeaderDefaultAppearance(HeaderResources resources, Template template) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);

    this.template = template;
  }

  @Override
  public XElement getBarElem(XElement parent) {
    return parent.getChild(1).cast();
  }

  @Override
  public XElement getHeadingElem(XElement parent) {
    return parent.getChild(2).cast();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void setIcon(XElement parent, ImageResource icon) {
    XElement iconWrap = parent.getFirstChildElement().cast();
    iconWrap.removeChildren();
    if (icon != null) {
      iconWrap.appendChild(IconHelper.getElement(icon));
    }
    parent.setClassName(style.headerHasIcon(), icon != null);
  }

}
