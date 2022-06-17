/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.TabPanel.TabPanelBottomAppearance;

public class GrayTabPanelBottomAppearance extends GrayTabPanelAppearance implements TabPanelBottomAppearance {

  public interface BottomTemplate extends Template {

    @XTemplate(source = "TabPanelBottom.html")
    SafeHtml render(TabPanelStyle style);

  }

  public GrayTabPanelBottomAppearance() {
    this(GWT.<GrayTabPanelResources> create(GrayTabPanelResources.class), GWT
        .<BottomTemplate> create(BottomTemplate.class), GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public GrayTabPanelBottomAppearance(GrayTabPanelResources resources, BottomTemplate template,
      ItemTemplate itemTemplate) {
    super(resources, template, itemTemplate);
  }

  @Override
  public XElement getBar(XElement parent) {
    return parent.getLastChild().cast();
  }

  @Override
  public void onScrolling(XElement parent, boolean scrolling) {
    parent.selectNode("." + style.tabFooter()).setClassName(style.tabScrolling(), scrolling);
  }

  @Override
  public XElement getBody(XElement parent) {
    return parent.getFirstChildElement().cast();
  }

  public XElement getStrip(XElement parent) {
    return getBar(parent).selectNode("." + style.tabStripBottom());
  }

}
