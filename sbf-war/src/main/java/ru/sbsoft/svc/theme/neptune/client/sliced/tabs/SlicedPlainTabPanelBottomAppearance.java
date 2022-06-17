/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.widget.core.client.PlainTabPanel.PlainTabPanelBottomAppearance;

/**
 *
 */
public class SlicedPlainTabPanelBottomAppearance extends SlicedTabPanelBottomAppearance implements PlainTabPanelBottomAppearance {

  public interface SlicedPlainTabPanelBottomResources extends SlicedTabPanelBottomResources {
    @Override
    @Source({"SlicedTabPanelBottom.gss", "SlicedPlainTabPanelBottom.gss"})
    SlicedPlainTabPanelBottomStyle style();
  }

  public interface SlicedPlainTabPanelBottomStyle extends SlicedTabPanelBottomStyle {
    String tabStripSpacer();
  }

  public interface SlicedPlainTabPanelBottomTemplates extends XTemplates {
    @XTemplate(source = "SlicedPlainTabPanelBottom.html")
    SafeHtml render(SlicedPlainTabPanelBottomStyle style);
  }

  private final SlicedPlainTabPanelBottomTemplates template;
  private final SlicedPlainTabPanelBottomStyle style;

  public SlicedPlainTabPanelBottomAppearance() {
    this(GWT.<SlicedPlainTabPanelBottomResources>create(SlicedPlainTabPanelBottomResources.class));
  }

  public SlicedPlainTabPanelBottomAppearance(SlicedPlainTabPanelBottomResources resources) {
    this(resources, GWT.<SlicedPlainTabPanelBottomTemplates>create(SlicedPlainTabPanelBottomTemplates.class));
  }

  public SlicedPlainTabPanelBottomAppearance(SlicedPlainTabPanelBottomResources resources, SlicedPlainTabPanelBottomTemplates template) {
    super(resources);
    this.style = resources.style();
    this.template = template;
  }


  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render(style));
  }
}
