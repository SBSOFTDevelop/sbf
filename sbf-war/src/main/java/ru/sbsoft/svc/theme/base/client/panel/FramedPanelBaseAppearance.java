/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.panel;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.Style.Side;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.ThemeStyles;
import ru.sbsoft.svc.theme.base.client.frame.CollapsibleFrame;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame.NestedDivFrameResources;
import ru.sbsoft.svc.widget.core.client.FramedPanel.FramedPanelAppearance;

public abstract class FramedPanelBaseAppearance extends ContentPanelBaseAppearance implements FramedPanelAppearance {

  public interface FramedPanelDivFrameResources extends NestedDivFrameResources {

  }

  public interface FramedPanelTemplate extends ContentPanelTemplate {
    @XTemplate(source = "FramedPanel.html")
    SafeHtml render(ContentPanelStyle style);
  }

  protected CollapsibleFrame frame;


  public FramedPanelBaseAppearance(ContentPanelResources resources, FramedPanelTemplate template, CollapsibleFrame frame) {
    super(resources, template);

    this.frame = frame;
  }

  @Override
  public XElement getBodyWrap(XElement parent) {
    return frame.getCollapseElem(parent);
  }

  @Override
  public XElement getContentElem(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public int getFrameHeight(XElement parent) {
    int h = frame.getFrameSize(parent).getHeight();
    h += frame.getContentElem(parent).getFrameSize().getHeight();
    return h;
  }

  @Override
  public int getFrameWidth(XElement parent) {
    int w = frame.getFrameSize(parent).getWidth();

    XElement content = getContentElem(parent);
    w += content.getFrameWidth(Side.LEFT, Side.RIGHT);

    return w;
  }

  @Override
  public XElement getHeaderElem(XElement parent) {
    return frame.getHeaderElem(parent);
  }

  @Override
  public void onBodyBorder(XElement parent, boolean border) {
    getContentElem(parent).setClassName(ThemeStyles.get().style().border(), border);
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    parent.setClassName("noheader", hide);
    frame.onHideHeader(parent, hide);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

}
