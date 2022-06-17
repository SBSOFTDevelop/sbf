/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.TabPanel.TabPanelBottomAppearance;

/**
 *
 */
public class SlicedTabPanelBottomAppearance extends SlicedTabPanelAppearance implements TabPanelBottomAppearance {

  public interface SlicedTabPanelBottomTemplate extends XTemplates {
    @XTemplate(source = "SlicedTabPanelBottom.html")
    SafeHtml render(SlicedTabPanelBottomStyle style);
  }

  public interface SlicedTabPanelBottomResources extends SlicedTabPanelResources {
    @Source("SlicedTabPanelBottom.gss")
    @Override
    SlicedTabPanelBottomStyle style();

    @Override
    @Source("inactive-bottom-tab-l.png")
    ImageResource tabLeft();


    @Override
    @Source("bottom-tab-l.png")
    ImageResource tabLeftOver();

    @Override
    @Source("bottom-tab-l.png")
    ImageResource tabLeftActive();

    @Override
    @Source("inactive-bottom-tab-r.png")
    ImageResource tabRight();

    @Override
    @Source("bottom-tab-r.png")
    ImageResource tabRightOver();

    @Override
    @Source("bottom-tab-r.png")
    ImageResource tabRightActive();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("inactive-bottom-tab-bg.png")
    ImageResource tabCenter();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("bottom-tab-bg.png")
    ImageResource tabCenterOver();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("bottom-tab-bg.png")
    ImageResource tabCenterActive();


    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("bottom-tab-strip-bg.png")
    ImageResource tabStripBackground();
  }

  public interface SlicedTabPanelBottomStyle extends SlicedTabPanelStyle {
  }

  private final SlicedTabPanelBottomTemplate template;
  private final SlicedTabPanelBottomStyle style;

  public SlicedTabPanelBottomAppearance() {
    this(GWT.<SlicedTabPanelBottomResources>create(SlicedTabPanelBottomResources.class));
  }

  public SlicedTabPanelBottomAppearance(SlicedTabPanelBottomResources resources) {
    this(resources, GWT.<SlicedTabPanelBottomTemplate>create(SlicedTabPanelBottomTemplate.class));
  }

  public SlicedTabPanelBottomAppearance(SlicedTabPanelBottomResources resources, SlicedTabPanelBottomTemplate template) {
    super(resources);
    this.template = template;
    this.style = resources.style();
  }

  @Override
  public XElement getBar(XElement parent) {
    return parent.getLastChild().cast();
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render((SlicedTabPanelBottomStyle) style));
  }
}
