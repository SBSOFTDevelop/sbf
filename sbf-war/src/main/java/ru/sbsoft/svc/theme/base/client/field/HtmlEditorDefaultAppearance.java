/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.form.Field.FieldStyles;
import ru.sbsoft.svc.widget.core.client.form.HtmlEditor.HtmlEditorAppearance;

public class HtmlEditorDefaultAppearance implements HtmlEditorAppearance {

  public interface HtmlEditorResources extends ClientBundle {
    @Source("tb-source.gif")
    ImageResource source();

    
    @Source("tb-bold.gif")
    ImageResource bold();
    
    @Source("tb-font-color.gif")
    ImageResource fontColor();
    
    @Source("tb-font-decrease.gif")
    ImageResource fontDecrease();
    
    @Source("tb-font-highlight.gif")
    ImageResource fontHighlight();
    
    @Source("tb-font-increase.gif")
    ImageResource fontIncrease();
    
    @Source("tb-italic.gif")
    ImageResource italic();
    
    @Source("tb-justify-center.gif")
    ImageResource justifyCenter();
    
    @Source("tb-justify-left.gif")
    ImageResource justifyLeft();
    
    @Source("tb-justify-right.gif")
    ImageResource justifyRight();
    
    @Source("tb-link.gif")
    ImageResource link();
    
    @Source("tb-ol.gif")
    ImageResource ol();
    
    @Source("tb-ul.gif")
    ImageResource ul();
    
    @Source("tb-underline.gif")
    ImageResource underline();
    
    @Source("HtmlEditor.gss")
    HtmlEditorStyle css();
  }
  
  public interface HtmlEditorStyle extends CssResource, FieldStyles {
    String editor();
    
    String frame();
  }
  
  private final HtmlEditorResources resources;
  private final HtmlEditorStyle style;
  
  public HtmlEditorDefaultAppearance() {
    this(GWT.<HtmlEditorResources>create(HtmlEditorResources.class));
  }
  
  public HtmlEditorDefaultAppearance(HtmlEditorResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
    
    StyleInjectorHelper.ensureInjected(style, true);
  }
  
  @Override
  public ImageResource bold() {
    return resources.bold();
  }

  @Override
  public ImageResource fontColor() {
    return resources.fontColor();
  }

  @Override
  public ImageResource fontDecrease() {
    return resources.fontDecrease();
  }

  @Override
  public ImageResource fontHighlight() {
    return resources.fontHighlight();
  }

  @Override
  public ImageResource fontIncrease() {
    return resources.fontIncrease();
  }

  @Override
  public Element getContentElement(XElement parent) {
    return parent;
  }

  @Override
  public ImageResource italic() {
    return resources.italic();
  }

  @Override
  public ImageResource justifyCenter() {
    return resources.justifyCenter();
  }

  @Override
  public ImageResource justifyLeft() {
    return resources.justifyLeft();
  }

  @Override
  public ImageResource justifyRight() {
    return resources.justifyRight();
  }

  @Override
  public ImageResource link() {
    return resources.link();
  }

  @Override
  public ImageResource ol() {
    return resources.ol();
  }

  @Override
  public ImageResource ul() {
    return resources.ul();
  }

  @Override
  public ImageResource underline() {
    return resources.underline();
  }

  @Override
  public String editor() {
    return style.editor();
  }

  @Override
  public String frame() {
    return style.frame();
  }

  @Override
  public ImageResource source() {
    return resources.source();
  }

}
