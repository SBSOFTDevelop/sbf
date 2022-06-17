/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.Size;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3ContentPanelAppearance.ThemeDetailsBundle;
import ru.sbsoft.svc.widget.core.client.FramedPanel.FramedPanelAppearance;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;

public class Css3FramedPanelAppearance implements FramedPanelAppearance {

  public interface Css3FramedPanelResources extends ClientBundle {
    @Source("Css3FramedPanel.gss")
    Css3FramedPanelStyle style();

    ThemeDetails theme();
  }

  public interface FramedPanelTemplate extends XTemplates {
    @XTemplate(source = "Css3FramedPanel.html")
    SafeHtml render(Css3FramedPanelStyle style);
  }

  public interface Css3FramedPanelStyle extends CssResource {
    String body();

    String bodyWrap();

    String footer();

    String header();

    String panel();

    String noHeader();
  }

  private Css3FramedPanelResources resources;
  private Css3FramedPanelStyle style;
  private final FramedPanelTemplate template;
  protected final ThemeDetails theme;

  public Css3FramedPanelAppearance() {
    this(GWT.<Css3FramedPanelResources>create(Css3FramedPanelResources.class));
  }

  public Css3FramedPanelAppearance(Css3FramedPanelResources resources) {
    this(resources, GWT.<FramedPanelTemplate>create(FramedPanelTemplate.class));
  }

  public Css3FramedPanelAppearance(Css3FramedPanelResources resources, FramedPanelTemplate template) {
    this.resources = resources;
    this.style = this.resources.style();
    this.template = template;

    StyleInjectorHelper.ensureInjected(this.style, true);

    ThemeDetailsBundle bundle = GWT.create(ThemeDetailsBundle.class);
    this.theme = bundle.theme();
  }

  @Override
  public int getFrameHeight(XElement parent) {
    return Math.max(theme.framedPanel().borderRadius(), theme.framedPanel().border().top())
        + Math.max(theme.framedPanel().borderRadius(), theme.framedPanel().border().bottom());
  }

  @Override
  public int getFrameWidth(XElement parent) {
    return Math.max(theme.framedPanel().borderRadius(), theme.framedPanel().border().left())
        + Math.max(theme.framedPanel().borderRadius(), theme.framedPanel().border().right());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public HeaderAppearance getHeaderAppearance() {
    return new Css3HeaderAppearance();
  }

  @Override
  public IconConfig collapseIcon() {
    return ToolButton.UP;
  }

  @Override
  public IconConfig expandIcon() {
    return ToolButton.DOWN;
  }

  @Override
  public XElement getBodyWrap(XElement parent) {
    return parent.selectNode("." + style.bodyWrap());
  }

  @Override
  public XElement getContentElem(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public XElement getFooterElem(XElement parent) {
    return parent.selectNode("." + style.footer());
  }

  @Override
  public XElement getHeaderElem(XElement parent) {
    return parent.selectNode("." + style.header());
  }

  @Override
  public void onBodyBorder(XElement parent, boolean border) {
    getContentElem(parent).applyStyles(!border ? "border: 0px" : "");
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    parent.selectNode("." + style.header()).setVisible(!hide);
    parent.setClassName(style.noHeader(), hide);
  }

  @Override
  public Size getHeaderSize(XElement parent) {
    Element head = parent.getFirstChildElement();
    return new Size(head.getOffsetWidth(), head.getOffsetHeight());
  }

}
