/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.panel;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.panel.AccordionLayoutBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class BlueAccordionLayoutAppearance extends AccordionLayoutBaseAppearance {
  
  public interface BlueAccordionLayoutResources extends ContentPanelResources {

    @Source({"ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss", "BlueContentPanel.gss"})
    @Override
    BlueAccordionLayoutStyle style();

  }

  public interface BlueAccordionLayoutStyle extends ContentPanelStyle {

  }
  
  public BlueAccordionLayoutAppearance() {
    super(GWT.<BlueAccordionLayoutResources> create(BlueAccordionLayoutResources.class));
  }

  public BlueAccordionLayoutAppearance(BlueAccordionLayoutResources resources) {
    super(resources);
  }
  
  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new BlueAccordionHeaderAppearance();
  }
}
