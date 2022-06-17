/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.container;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.container.Css3AccordionLayoutAppearance.Css3AccordionHeaderResources;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance.Css3HeaderResources;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3HeaderAppearance.Css3HeaderStyle;
import ru.sbsoft.svc.theme.neptune.client.sliced.panel.SlicedContentPanelAppearance;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;

public class SlicedAccordionLayoutAppearance extends SlicedContentPanelAppearance implements AccordionLayoutAppearance {
  public interface SlicedAccordionLayoutResources extends SlicedContentPanelResources {
    @Override
    @Source("accordionlayout-background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();

    @Override
    @Source("SlicedAccordionLayout.gss")
    SlicedAccordionLayoutStyle style();
  }
  public interface SlicedAccordionLayoutStyle extends SlicedContentPanelStyle {

  }

  public interface SlicedAccordionHeaderResources extends Css3HeaderResources {
    @Source("accordionlayout-background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "SlicedAccordionHeader.gss"})
    SlicedAccordionHeaderStyle style();
  }
  public interface SlicedAccordionHeaderStyle extends Css3HeaderStyle {

  }

  public SlicedAccordionLayoutAppearance() {
    this(GWT.<SlicedAccordionLayoutResources>create(SlicedAccordionLayoutResources.class));
  }

  public SlicedAccordionLayoutAppearance(SlicedAccordionLayoutResources resources) {
    super(resources);
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
