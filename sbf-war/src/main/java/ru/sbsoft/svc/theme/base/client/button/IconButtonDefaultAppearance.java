/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconButtonAppearance;

public  class IconButtonDefaultAppearance implements IconButtonAppearance {

  public interface ToolButtonResources extends ClientBundle {
    
    @Source("IconButton.gss")
    IconButtonStyle style();
  }
  
  public interface IconButtonStyle extends CssResource {
    String button();
  }
  

  public interface Template extends XTemplates {
    @XTemplate(source = "IconButton.html")
    SafeHtml render(IconButtonStyle style);
  }

  private final Template template;
  private final IconButtonStyle style;
  
  public IconButtonDefaultAppearance() {
    this(GWT.<ToolButtonResources>create(ToolButtonResources.class));
  }
  
  public IconButtonDefaultAppearance(ToolButtonResources resources) {
    this.style = resources.style();
    this.style.ensureInjected();
    
    this.template = GWT.create(Template.class);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public XElement getIconElem(XElement parent) {
    return parent;
  }

}
