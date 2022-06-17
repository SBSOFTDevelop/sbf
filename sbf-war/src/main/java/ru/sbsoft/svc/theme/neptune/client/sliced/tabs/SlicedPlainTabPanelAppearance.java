/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.widget.core.client.PlainTabPanel.PlainTabPanelAppearance;

public class SlicedPlainTabPanelAppearance extends SlicedTabPanelAppearance implements PlainTabPanelAppearance {
  public interface PlainPanelTemplate extends XTemplates {
    @XTemplate(source = "SlicedPlainTabPanel.html")
    SafeHtml renderPlain(SlicedPlainTabPanelStyle style);
  }

  public interface SlicedPlainTabPanelResources extends SlicedTabPanelResources {
    @Source({"SlicedTabPanel.gss", "SlicedPlainTabPanel.gss"})
    @Override
    SlicedPlainTabPanelStyle style();
  }

  public interface SlicedPlainTabPanelStyle extends SlicedTabPanelStyle {
    String tabStripSpacer();
  }

  private final PlainPanelTemplate template;

  public SlicedPlainTabPanelAppearance() {
    this(GWT.<SlicedPlainTabPanelResources>create(SlicedPlainTabPanelResources.class));
  }

  public SlicedPlainTabPanelAppearance(SlicedPlainTabPanelResources resources) {
    this(resources, GWT.<PlainPanelTemplate>create(PlainPanelTemplate.class));
  }

  public SlicedPlainTabPanelAppearance(SlicedPlainTabPanelResources resources, PlainPanelTemplate template) {
    super(resources);

    this.template = template;
  }


  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.renderPlain((SlicedPlainTabPanelStyle) resources.style()));
  }
}
