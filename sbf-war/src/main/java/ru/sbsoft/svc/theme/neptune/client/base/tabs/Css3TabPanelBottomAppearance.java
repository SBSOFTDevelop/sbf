/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.TabPanel.TabPanelBottomAppearance;

public class Css3TabPanelBottomAppearance extends Css3TabPanelAppearance implements TabPanelBottomAppearance {

  public interface Css3TabPanelBottomTemplate extends XTemplates {
    @XTemplate(source = "Css3TabPanelBottom.html")
    SafeHtml render(Css3TabPanelBottomStyle style);
  }

  public interface Css3TabPanelBottomResources extends Css3TabPanelResources {
    @Source("Css3TabPanelBottom.gss")
    @Override
    Css3TabPanelBottomStyle style();
  }

  public interface Css3TabPanelBottomStyle extends Css3TabPanelStyle {
  }

  private final Css3TabPanelBottomTemplate template;

  public Css3TabPanelBottomAppearance() {
    this(GWT.<Css3TabPanelBottomResources>create(Css3TabPanelBottomResources.class));
  }

  public Css3TabPanelBottomAppearance(Css3TabPanelBottomResources resources) {
    this(resources, GWT.<Css3TabPanelBottomTemplate>create(Css3TabPanelBottomTemplate.class));
  }

  public Css3TabPanelBottomAppearance(Css3TabPanelBottomResources resources, Css3TabPanelBottomTemplate template) {
    super(resources);
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render((Css3TabPanelBottomStyle) style));
  }

  @Override
  public XElement getBar(XElement parent) {
    return parent.getLastChild().cast();
  }

  public XElement getStrip(XElement parent) {
    return getBar(parent).selectNode("." + style.tabStrip());
  }
}
