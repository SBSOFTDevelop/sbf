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
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.box.MessageBox.MessageBoxAppearance;


public class MessageBoxDefaultAppearance implements MessageBoxAppearance {

  public interface MessageBoxResources extends ClientBundle {
    
    @Source("MessageBox.gss")
    MessageBoxBaseStyle style();
  }
  
  public interface MessageBoxBaseStyle extends CssResource {
    String content();
    
    String icon();
    
    String message();
  }
  
  interface Template extends XTemplates {
    @XTemplate(source = "MessageBox.html")
    SafeHtml render(MessageBoxBaseStyle style);
  }
  
  protected MessageBoxResources resources;
  protected MessageBoxBaseStyle style;
  protected Template template;

  public MessageBoxDefaultAppearance() {
    this(GWT.<MessageBoxResources>create(MessageBoxResources.class));
  }
  
  public MessageBoxDefaultAppearance(MessageBoxResources resources) {
    this.resources = resources;
    this.style = resources.style();
    
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = GWT.<Template>create(Template.class);
  }

  @Override
  public XElement getContentElement(XElement parent) {
    return parent.selectNode("." + style.content());
  }

  @Override
  public XElement getIconElement(XElement parent) {
    return parent.selectNode("." + style.icon());
  }

  @Override
  public XElement getMessageElement(XElement parent) {
    return parent.selectNode("." + style.message());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }
  
}
