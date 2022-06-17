/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.widget.core.client.PlainTabPanel.PlainTabPanelBottomAppearance;

/**
 *
 */
public class Css3PlainTabPanelBottomAppearance extends Css3TabPanelBottomAppearance implements PlainTabPanelBottomAppearance {

  public interface Css3PlainTabPanelBottomResources extends Css3TabPanelBottomResources {
    @Override
    @Source({"Css3TabPanelBottom.gss", "Css3PlainTabPanelBottom.gss"})
    Css3PlainTabPanelBottomStyle style();

    ImageResource plainTabClose();

    ImageResource plainTabCloseOver();
  }

  public interface Css3PlainTabPanelBottomStyle extends Css3TabPanelBottomStyle {
    String tabStripSpacer();
  }

  public interface Css3PlainTabPanelBottomTemplates extends XTemplates {
    @XTemplate(source = "Css3PlainTabPanelBottom.html")
    SafeHtml render(Css3PlainTabPanelBottomStyle style);
  }

  private final Css3PlainTabPanelBottomTemplates template;
  private final Css3PlainTabPanelBottomStyle style;

  public Css3PlainTabPanelBottomAppearance() {
    this(GWT.<Css3PlainTabPanelBottomResources>create(Css3PlainTabPanelBottomResources.class));
  }

  public Css3PlainTabPanelBottomAppearance(Css3PlainTabPanelBottomResources resources) {
    this(resources, GWT.<Css3PlainTabPanelBottomTemplates>create(Css3PlainTabPanelBottomTemplates.class));
  }

  public Css3PlainTabPanelBottomAppearance(Css3PlainTabPanelBottomResources resources, Css3PlainTabPanelBottomTemplates template) {
    super(resources);
    this.style = resources.style();
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render(style));
  }
}
