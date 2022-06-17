/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.widget.core.client.PlainTabPanel.PlainTabPanelAppearance;

/**
 *
 */
public class Css3PlainTabPanelAppearance extends Css3TabPanelAppearance implements PlainTabPanelAppearance {

  public interface Css3PlainTabPanelResources extends Css3TabPanelResources {
    @Override
    @Source({"Css3TabPanel.gss", "Css3PlainTabPanel.gss"})
    Css3PlainTabPanelStyle style();

    ImageResource activePlainTabClose();

    ImageResource activePlainTabCloseOver();

    ImageResource plainTabClose();

    ImageResource plainTabCloseOver();
  }

  public interface Css3PlainTabPanelStyle extends Css3TabPanelStyle {

    String tabStripSpacer();
  }

  public interface Css3PlainTabPanelTemplates extends Css3TabPanelTemplates {
    @Override
    @XTemplate(source = "Css3TabPanel.html")
    SafeHtml render(Css3TabPanelStyle style);

    @XTemplate(source = "Css3PlainTabPanel.html")
    SafeHtml renderPlain(Css3PlainTabPanelStyle style);
  }

  private final Css3PlainTabPanelTemplates template;
  private final Css3PlainTabPanelStyle style;

  public Css3PlainTabPanelAppearance() {
    this(GWT.<Css3PlainTabPanelResources>create(Css3PlainTabPanelResources.class));
  }

  public Css3PlainTabPanelAppearance(Css3PlainTabPanelResources resources) {
    this(resources, GWT.<Css3PlainTabPanelTemplates>create(Css3PlainTabPanelTemplates.class));
  }

  public Css3PlainTabPanelAppearance(Css3PlainTabPanelResources resources, Css3PlainTabPanelTemplates template) {
    super(resources, template);
    this.style = resources.style();
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.renderPlain(style));
  }
}
