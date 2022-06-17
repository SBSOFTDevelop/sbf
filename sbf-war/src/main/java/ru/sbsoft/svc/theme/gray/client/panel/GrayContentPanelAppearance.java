/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.panel;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.panel.ContentPanelBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class GrayContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface GrayContentPanelResources extends ContentPanelResources {

    @Source({"ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss", "GrayContentPanel.gss"})
    @Override
    GrayContentPanelStyle style();

  }

  public interface GrayContentPanelStyle extends ContentPanelStyle {

  }

  public GrayContentPanelAppearance() {
    super(GWT.<GrayContentPanelResources> create(GrayContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public GrayContentPanelAppearance(GrayContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new GrayHeaderAppearance();
  }

}
