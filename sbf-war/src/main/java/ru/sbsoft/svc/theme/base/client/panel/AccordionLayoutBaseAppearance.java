/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.panel;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;

public abstract class AccordionLayoutBaseAppearance extends ContentPanelBaseAppearance implements AccordionLayoutAppearance{

  public AccordionLayoutBaseAppearance(ContentPanelResources resources) {
    super(resources);
  }
  
  @Override
  public HeaderAppearance getHeaderAppearance() {
    return GWT.create(HeaderAppearance.class);
  }
  
  @Override
  public IconConfig collapseIcon() {
    return ToolButton.MINUS;
  }
  
  @Override
  public IconConfig expandIcon() {
    return ToolButton.PLUS;
  }

}
