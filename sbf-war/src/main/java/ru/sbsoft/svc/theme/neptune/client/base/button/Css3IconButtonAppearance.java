/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.button;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconButtonAppearance;

public class Css3IconButtonAppearance implements IconButtonAppearance {
  public interface Css3ToolButtonResources extends ClientBundle {
    @Source("Css3IconButton.gss")
    Css3IconButtonStyle style();
  }

  public interface Css3IconButtonStyle extends CssResource {
    String button();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "Css3IconButton.html")
    SafeHtml render(Css3IconButtonStyle style);
  }

  private final Template template;
  private final Css3IconButtonStyle style;

  public Css3IconButtonAppearance() {
    this(GWT.<Css3ToolButtonResources>create(Css3ToolButtonResources.class));
  }

  public Css3IconButtonAppearance(Css3ToolButtonResources resources) {
    this.style = resources.style();
    StyleInjectorHelper.ensureInjected(this.style, false);

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
