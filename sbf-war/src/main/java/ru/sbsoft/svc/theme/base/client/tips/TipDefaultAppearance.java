/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.tips;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.Style.Side;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame.NestedDivFrameResources;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import ru.sbsoft.svc.widget.core.client.tips.Tip.TipAppearance;

public class TipDefaultAppearance implements TipAppearance {

  public interface TipDefaultTemplate extends XTemplates {

    @XTemplate(source = "TipDefault.html")
    SafeHtml render(TipStyle style);

  }

  public interface TipDivFrameResources extends ClientBundle, NestedDivFrameResources {

    ImageResource anchorBottom();

    ImageResource anchorLeft();

    ImageResource anchorRight();

    ImageResource anchorTop();

    @Source("background.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @Source({"ru/sbsoft/svc/theme/base/client/frame/NestedDivFrame.gss", "TipDivFrame.gss"})
    @Override
    TipNestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Override
    ImageResource topLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

  }

  public interface TipNestedDivFrameStyle extends NestedDivFrameStyle {
  }

  public interface TipResources extends ClientBundle {

    ImageResource anchorBottom();

    ImageResource anchorLeft();

    ImageResource anchorRight();

    ImageResource anchorTop();

    @Source("TipDefault.gss")
    TipStyle style();

  }

  public interface TipStyle extends CssResource {

    String anchor();

    String anchorBottom();

    String anchorLeft();

    String anchorRight();

    String anchorTop();

    String heading();

    String text();

    String tools();

    String tip();
    
  }

  protected final TipResources resources;
  protected final TipStyle style;
  protected TipDefaultTemplate template;

  protected Frame frame;

  public TipDefaultAppearance() {
    this(GWT.<TipResources> create(TipResources.class));
  }

  public TipDefaultAppearance(TipResources resources) {
    this(resources, GWT.<TipDefaultTemplate> create(TipDefaultTemplate.class));
  }

  public TipDefaultAppearance(TipResources resources, TipDefaultTemplate template) {
    this.resources = resources;
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(style, true);

    this.template = template;

    frame = new NestedDivFrame(GWT.<TipDivFrameResources> create(TipDivFrameResources.class));
  }

  @Override
  public void applyAnchorDirectionStyle(XElement anchorEl, Side anchor) {
    anchorEl.setClassName(style.anchorTop(), anchor == Side.TOP);
    anchorEl.setClassName(style.anchorBottom(), anchor == Side.BOTTOM);
    anchorEl.setClassName(style.anchorRight(), anchor == Side.RIGHT);
    anchorEl.setClassName(style.anchorLeft(), anchor == Side.LEFT);
  }

  @Override
  public void applyAnchorStyle(XElement anchorEl) {
    anchorEl.addClassName(style.anchor());
  }

  public XElement getHeaderElement(XElement parent) {
    return parent.selectNode("." + style.heading());
  }

  @Override
  public XElement getBodyElement(XElement parent) {
    return parent.selectNode("." + style.text());
  }

  @Override
  public XElement getToolsElement(XElement parent) {
    return parent.selectNode("." + style.tools());
  }

  @Override
  public void removeAnchorStyle(XElement anchorEl) {
    anchorEl.removeClassName(style.anchor());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

  protected TipDivFrameResources getDivFrameResources() {
    return GWT.create(TipDivFrameResources.class);
  }

  @Override
  public void updateContent(XElement parent, SafeHtml title, SafeHtml body) {
    XElement header = getHeaderElement(parent);
    if (title == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      header.getParentElement().getStyle().setDisplay(Display.NONE);
    } else {
      header.setInnerSafeHtml(title);
      header.getParentElement().getStyle().setDisplay(Display.BLOCK);
    }

    getBodyElement(parent).setInnerSafeHtml(body);
  }

}
