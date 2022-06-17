/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.container.Viewport.ViewportAppearance;

public class ViewportDefaultAppearance implements ViewportAppearance {

  public interface ViewportResources extends ClientBundle {

    @Source("Viewport.gss")
    ViewportStyle style();

  }

  public interface ViewportStyle extends CssResource {

    String viewport();

  }

  public interface ViewportTemplate extends XTemplates {

    @XTemplate("<div class=\"{style.viewport}\"></div>")
    SafeHtml template(ViewportStyle style);

  }

  private final ViewportResources resources;
  private final ViewportTemplate templates;
  
  public ViewportDefaultAppearance() {
    this(GWT.<ViewportResources>create(ViewportResources.class), GWT.<ViewportTemplate>create(ViewportTemplate.class));
  }

  public ViewportDefaultAppearance(ViewportResources resources, ViewportTemplate templates) {
    this.resources = resources;
    this.templates = templates;
    
    StyleInjectorHelper.ensureInjected(resources.style(), true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(templates.template(resources.style()));
  }

}
