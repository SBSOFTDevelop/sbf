/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.panel;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.panel.AccordionLayoutBaseAppearance;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class GrayAccordionLayoutAppearance extends AccordionLayoutBaseAppearance {
  
  public interface GrayAccordionLayoutResources extends ContentPanelResources {

    @Source({"ru/sbsoft/svc/theme/base/client/panel/ContentPanel.gss", "GrayContentPanel.gss"})
    @Override
    GrayAccordionLayoutStyle style();

  }

  public interface GrayAccordionLayoutStyle extends ContentPanelStyle {

  }
  
  public GrayAccordionLayoutAppearance() {
    super(GWT.<GrayAccordionLayoutResources> create(GrayAccordionLayoutResources.class));
  }

  public GrayAccordionLayoutAppearance(GrayAccordionLayoutResources resources) {
    super(resources);
  }
  
  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new GrayAccordionHeaderAppearance();
  }
}
