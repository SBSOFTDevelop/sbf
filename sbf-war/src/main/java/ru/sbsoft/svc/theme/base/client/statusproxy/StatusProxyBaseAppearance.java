/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.statusproxy;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.dnd.core.client.StatusProxy.StatusProxyAppearance;

public abstract class StatusProxyBaseAppearance implements StatusProxyAppearance {

  public interface StatusProxyResources {

    ImageResource dropAllowed();

    ImageResource dropNotAllowed();

    StatusProxyStyle style();

  }

  public interface StatusProxyStyle extends CssResource {

    String dragGhost();

    String dropAllowed();

    String dropDisallowed();

    String dropIcon();

    String proxy();

  }

  public interface StatusProxyTemplates extends XTemplates {

    @XTemplate("<div class=\"{style.proxy}\"><div class=\"{style.dropIcon}\"></div><div class=\"{style.dragGhost}\"></div></div>")
    SafeHtml template(StatusProxyBaseAppearance.StatusProxyStyle style);

  }

  private final StatusProxyTemplates templates;
  private final StatusProxyResources resources;
  private final StatusProxyStyle style;

  public StatusProxyBaseAppearance(StatusProxyBaseAppearance.StatusProxyResources resources,
      StatusProxyBaseAppearance.StatusProxyTemplates templates) {
    this.resources = resources;
    style = resources.style();
    this.templates = templates;

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(templates.template(style));
  }

  @Override
  public void setStatus(Element parent, boolean allowed) {
    if (allowed) {
      setStatus(parent, resources.dropAllowed());
    } else {
      setStatus(parent, resources.dropNotAllowed());
    }
  }

  @Override
  public void setStatus(Element parent, ImageResource icon) {
    XElement wrap = iconWrap(parent);
    wrap.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    if (icon != null) {
      wrap.appendChild(getImage(icon));
    }
  }

  @Override
  public void update(Element parent, SafeHtml html) {
    getDragGhost(parent).setInnerSafeHtml(html);
  }

  protected XElement iconWrap(Element parent) {
    return parent.getFirstChildElement().cast();
  }

  private Element getDragGhost(Element parent) {
    return XElement.as(parent).select("." + style.dragGhost()).getItem(0);
  }

  private Element getImage(ImageResource ir) {
    return AbstractImagePrototype.create(ir).createElement();
  }

}
