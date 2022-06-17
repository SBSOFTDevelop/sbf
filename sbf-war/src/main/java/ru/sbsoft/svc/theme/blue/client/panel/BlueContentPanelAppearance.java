/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.panel;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.panel.ContentPanelBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class BlueContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface BlueContentPanelResources extends ContentPanelResources {

    @Source({"ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss", "BlueContentPanel.gss"})
    @Override
    BlueContentPanelStyle style();

  }

  public interface BlueContentPanelStyle extends ContentPanelStyle {

  }

  public BlueContentPanelAppearance() {
    super(GWT.<BlueContentPanelResources> create(BlueContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public BlueContentPanelAppearance(BlueContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new BlueHeaderAppearance();
  }

}
