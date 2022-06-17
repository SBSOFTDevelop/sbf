/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.button;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.theme.base.client.frame.TableFrame.TableFrameStyle;
import ru.sbsoft.svc.widget.core.client.button.ButtonGroup.ButtonGroupAppearance;

public abstract class ButtonGroupBaseAppearance implements ButtonGroupAppearance {

  public interface ButtonGroupResources extends ClientBundle {
    @Source("ButtonGroup.gss")
    ButtonGroupStyle css();
  }

  public interface ButtonGroupStyle extends CssResource {
    String body();

    String group();

    String header();

    String text();

  }

  public interface ButtonGroupTableFrameStyle extends TableFrameStyle {
    String noheader();
  }

  public interface GroupTemplate extends XTemplates {
    @XTemplate("<div class='{style.group}'><div class='{style.body}'></div></div>")
    SafeHtml render(ButtonGroupStyle style);

    @XTemplate("<span class='{style.text}'>{html}</span>")
    SafeHtml renderHeading(SafeHtml html, ButtonGroupStyle style);
  }

  protected final ButtonGroupResources resources;
  protected final ButtonGroupStyle style;
  protected final Frame frame;
  protected final GroupTemplate template;

  public ButtonGroupBaseAppearance(ButtonGroupResources resources, GroupTemplate template, Frame frame) {
    this.resources = resources;
    this.template = template;
    this.frame = frame;

    this.style = this.resources.css();

    StyleInjectorHelper.ensureInjected(this.style, true);
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
    w += frame.getContentElem(parent).getFrameSize().getWidth();
    return w;
  }

  @Override
  public XElement getHeaderElement(XElement parent) {
    return parent.selectNode("." + style.header());
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    XElement head = frame.getHeaderElem(parent);
    if (head != null && head.getChildCount() > 0) {
      head.getFirstChildElement().getStyle().setDisplay(hide ? Display.NONE : Display.BLOCK);
    }
    // ButtonGroupTableFrameStyle s = getFrameResources().style();
    // parent.setClassName(s.noheader(), hide);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

  @Override
  public void setHeading(XElement parent, SafeHtml html) {
    frame.getHeaderElem(parent).setInnerSafeHtml(template.renderHeading(html, style));
  }

}
