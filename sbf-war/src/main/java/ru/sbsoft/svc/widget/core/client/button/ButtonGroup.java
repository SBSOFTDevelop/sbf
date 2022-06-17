/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Size;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;

public class ButtonGroup extends SimpleContainer {

  public interface ButtonGroupAppearance {
    void render(SafeHtmlBuilder sb);

    void setHeading(XElement parent, SafeHtml html);
    
    void onHideHeader(XElement parent, boolean hide);

    XElement getHeaderElement(XElement parent);

    XElement getContentElem(XElement parent);

    int getFrameHeight(XElement parent);

    int getFrameWidth(XElement parent);
  }

  private final ButtonGroupAppearance appearance;
  private SafeHtml heading = SafeHtmlUtils.EMPTY_SAFE_HTML;

  public ButtonGroup() {
    this(GWT.<ButtonGroupAppearance> create(ButtonGroupAppearance.class));
  }

  public ButtonGroup(ButtonGroupAppearance appearance) {
    super(true);
    this.appearance = appearance;

    setDeferHeight(true);

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));

//    addStyleName("x-toolbar-mark");
  }

  /**
   * Returns the heading html.
   *
   * @return the heading html
   */
  public SafeHtml getHeading() {
    return heading;
  }

  /**
   * Sets the heading html.
   *
   * @param html the heading html
   */
  public void setHeading(SafeHtml html) {
    this.heading = html;
    appearance.setHeading(getElement(), heading);
  }

  /**
   * Sets the heading text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  public void setHeading(String text) {
    setHeading(SafeHtmlUtils.fromString(text));
  }

  public ButtonGroupAppearance getAppearance() {
    return appearance;
  }

  public void setHeaderVisible(boolean visible) {
    appearance.onHideHeader(getElement(), !visible);
  }

  @Override
  protected void onResize(int width, int height) {
    Size frameSize = getFrameSize();

    if (isAutoWidth()) {
      getContainerTarget().getStyle().clearWidth();
    } else {
      width -= frameSize.getWidth();
      getContainerTarget().setWidth(width - frameSize.getWidth(), true);

    }

    if (isAutoHeight()) {
      getContainerTarget().getStyle().clearHeight();
    } else {
      height -= frameSize.getHeight();
      height -= appearance.getHeaderElement(getElement()).getOffsetHeight();
      getContainerTarget().setHeight(height - frameSize.getHeight(), true);
    }

    super.onResize(width, height);
  }

  @Override
  protected XElement getContainerTarget() {
    return appearance.getContentElem(getElement());
  }

  protected Size getFrameSize() {
    return new Size(appearance.getFrameWidth(getElement()), appearance.getFrameHeight(getElement()));
  }

}
