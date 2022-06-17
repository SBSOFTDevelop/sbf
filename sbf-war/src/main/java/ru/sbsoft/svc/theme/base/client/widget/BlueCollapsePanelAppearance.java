/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.Style.LayoutRegion;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.resources.ThemeStyles;
import ru.sbsoft.svc.theme.base.client.widget.CollapsePanelDefaultAppearance.CollapsePanelResources;
import ru.sbsoft.svc.theme.base.client.widget.CollapsePanelDefaultAppearance.CollapsePanelStyle;
import ru.sbsoft.svc.widget.core.client.CollapsePanel.CollapsePanelAppearance;

public class BlueCollapsePanelAppearance extends CollapsePanelDefaultAppearance {

  public interface BlueCollapsePanelResources extends CollapsePanelResources {

    @Source({"CollapsePanel.gss", "BlueCollapsePanel.gss"})
    BlueCollapsePanelStyle style();
  }

  public interface BlueCollapsePanelStyle extends CollapsePanelStyle {
  }

  public BlueCollapsePanelAppearance() {
    this(GWT.<BlueCollapsePanelResources>create(BlueCollapsePanelResources.class));
  }

  public BlueCollapsePanelAppearance(BlueCollapsePanelResources resources) {
    super(resources);
  }

}
