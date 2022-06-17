/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.container;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3ContentPanelAppearance;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance.Css3HeaderResources;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance.Css3HeaderStyle;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;

public class Css3AccordionLayoutAppearance extends Css3ContentPanelAppearance implements AccordionLayoutAppearance {

  public interface Css3AccordionResources extends Css3ContentPanelResources {
    @Override
    @Source({"Css3AccordionLayout.gss"})
    Css3AccordionStyle style();
  }

  public interface Css3AccordionStyle extends Css3ContentPanelStyle {

  }

  public interface Css3AccordionHeaderStyle extends Css3HeaderStyle {

  }

  public interface Css3AccordionHeaderResources extends Css3HeaderResources {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "Css3AccordionLayoutHeader.gss"})
    Css3AccordionHeaderStyle style();
  }

  public Css3AccordionLayoutAppearance() {
    this(GWT.<Css3AccordionResources>create(Css3AccordionResources.class));
  }

  public Css3AccordionLayoutAppearance(Css3AccordionResources resources) {
    this(resources, GWT.<Css3ContentPanelTemplate>create(Css3ContentPanelTemplate.class));
  }

  public Css3AccordionLayoutAppearance(Css3AccordionResources resources, Css3ContentPanelTemplate template) {
    super(resources, template);
  }

  @Override
  public HeaderAppearance getHeaderAppearance() {
    return new Css3HeaderAppearance(GWT.<Css3AccordionHeaderResources>create(Css3AccordionHeaderResources.class));
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
