/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.container.BorderLayoutContainer.BorderLayoutAppearance;

public abstract class BorderLayoutBaseAppearance implements BorderLayoutAppearance {

  public interface BorderLayoutResources extends ClientBundle {
    @Source("BorderLayout.gss")
    BorderLayoutStyle css();
  }

  public interface BorderLayoutStyle extends CssResource {
    String container();

    String child();
  }

  private final BorderLayoutResources resources;
  private final BorderLayoutStyle style;

  public BorderLayoutBaseAppearance() {
    this(GWT.<BorderLayoutResources> create(BorderLayoutResources.class));
  }

  public BorderLayoutBaseAppearance(BorderLayoutResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
    this.style.ensureInjected();
  }

  @Override
  public XElement getContainerTarget(XElement parent) {
    return parent;
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.appendHtmlConstant("<div class='" + style.container() + "'></div>");
  }

  @Override
  public void onInsert(Widget child) {
    child.addStyleName(style.child());
  }

  @Override
  public void onRemove(Widget child) {
    child.removeStyleName(style.child());
  }

}
