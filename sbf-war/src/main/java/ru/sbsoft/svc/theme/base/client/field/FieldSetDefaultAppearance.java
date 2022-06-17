/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.button.IconButton;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.form.FieldSet.FieldSetAppearance;

public class FieldSetDefaultAppearance implements FieldSetAppearance {

  public interface Template extends XTemplates {
    @XTemplate(source = "FieldSet.html")
    SafeHtml render(FieldSetStyle style, boolean isGecko);
  }
  
  public interface FieldSetResources extends ClientBundle {

    @Source({"FieldSet.gss"})
    FieldSetStyle css();

  }

  public interface FieldSetStyle extends CssResource {
    String fieldSet();
    
    String legend();
    
    String toolWrap();
    
    String header();
    
    String body();
    
    String collapsed();
    
    String noborder();
  }

  private final FieldSetResources resources;
  private final FieldSetStyle style;
  private final Template template;

  public FieldSetDefaultAppearance() {
    this(GWT.<FieldSetResources> create(FieldSetResources.class));
  }

  public FieldSetDefaultAppearance(FieldSetResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
   
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = GWT.create(Template.class);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style, SVC.isGecko()));
  }

  @Override
  public XElement getHeadingElement(XElement parent) {
    return parent.selectNode("." + style.header());
  }

  @Override
  public XElement getToolElement(XElement parent) {
    return parent.selectNode("." + style.toolWrap());
  }

  @Override
  public XElement getContainerTarget(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public IconButton.IconConfig collapseIcon() {
    return ToolButton.UP;
  }

  @Override
  public IconButton.IconConfig expandIcon() {
    return ToolButton.DOWN;
  }

  @Override
  public void onCollapse(XElement parent, boolean collapse) {
    
    parent.setClassName(style.collapsed(), collapse);
  }

}
