/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.frame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.Size;

/**
 * {@link Frame} and {@link CollapsibleFrame} implementation that creates its frame using 3 sets of 3 nested DIVs. See
 * NestedDivFrame.html and NestedDivFrame.gss. This implementation uses images to render rounded corners.
 * 
 * <p />
 * <code>NestedDivFrameResources</code> MUST be extended / implemented to provide the images for this frame.
 */
public class NestedDivFrame implements Frame, CollapsibleFrame {

  /**
   * Defines the required images of this frame. This interface must be extended or implemented as no images are
   * specified in the base theme. Although not a requirement, {@code ClientBundle} can be implemented if the images will
   * be {@code ClientBundle} based.
   */
  public interface NestedDivFrameResources {

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bottomBorder();

    ImageResource bottomLeftBorder();

    ImageResource bottomRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource rightBorder();

    NestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource topBorder();

    ImageResource topLeftBorder();

    ImageResource topRightBorder();
  }

  public interface NestedDivFrameStyle extends CssResource {

    String bodyWrap();

    String bottom();

    String bottomLeft();

    String bottomRight();

    String content();

    String contentArea();

    String left();

    String over();

    String pressed();

    String right();

    String top();

    String topLeft();

    String topRight();

  }

  public interface Template extends XTemplates {
    @XTemplate(source = "NestedDivFrame.html")
    SafeHtml render(NestedDivFrameStyle style, SafeHtml content);
  }

  private NestedDivFrameStyle style;
  private Template template = GWT.create(Template.class);
  private NestedDivFrameResources resources;

  public NestedDivFrame(NestedDivFrameResources resources) {
    this.resources = resources;
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public XElement getContentElem(XElement parent) {
    return parent.selectNode("." + style.content());
  }

  @Override
  public Size getFrameSize(XElement parent) {
    int h = resources.topLeftBorder().getHeight();
    int w = resources.topLeftBorder().getWidth();

    // EXTGWT-2074 workaround for framed content panel where header is part of
    // the frame
    // we assume if frame height > frame width then we have a header which
    // clears frame height
    if (h > w) {
      if (parent == null || !isHeaderHidden(parent)) {
        h = parent.getFirstChildElement().<XElement>cast().getFrameSize().getHeight();
      } else {
        h = getHeaderElem(parent).getOffsetHeight();
      }
    }

    // we can't get height of topBorder as it is includes the header, using
    // width of topLeftBorder assuming equally rounded corners
    return new Size(resources.leftBorder().getWidth() + resources.rightBorder().getWidth(), h
        + resources.bottomBorder().getHeight());
  }

  @Override
  public XElement getHeaderElem(XElement parent) {
    return parent.selectNode("." + style.top());
  }

  public NestedDivFrameResources getResources() {
    return resources;
  }

  @Override
  public void onFocus(XElement parent, boolean focus) {

  }

  public boolean isHeaderHidden(XElement parent) {
    XElement header = getHeaderElem(parent);
    if (header != null) {
      return header.hasClassName("x-hide-header");
    }
    return false;
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    XElement header = getHeaderElem(parent);
    if (header != null) {
      header.setClassName("x-hide-header", hide);
    }
    if (header != null && header.hasChildNodes()) {
      NodeList<Node> children = header.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node node = children.getItem(i);
        if (Element.is(node)) {
          Element.as(node).getStyle().setDisplay(hide ? Display.NONE : Display.BLOCK);
        }
      }
    }
  }

  @Override
  public void onOver(XElement parent, boolean over) {
    parent.setClassName(style.over(), over);
  }

  @Override
  public void onPress(XElement parent, boolean pressed) {
    parent.setClassName(style.pressed(), pressed);
  }

  @Override
  public String overClass() {
    return style.over();
  }

  @Override
  public String pressedClass() {
    return style.pressed();
  }

  @Override
  public void render(SafeHtmlBuilder builder, FrameOptions options, SafeHtml content) {
    builder.append(template.render(style, content));
  }

  @Override
  public XElement getCollapseElem(XElement parent) {
    return parent.selectNode("." + style.bodyWrap());
  }

}
