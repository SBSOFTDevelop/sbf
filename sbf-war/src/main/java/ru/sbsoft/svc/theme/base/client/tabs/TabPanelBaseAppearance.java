/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.tabs;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.resources.ThemeStyles;
import ru.sbsoft.svc.core.client.util.IconHelper;
import ru.sbsoft.svc.widget.core.client.TabItemConfig;
import ru.sbsoft.svc.widget.core.client.TabPanel.TabPanelAppearance;

public abstract class TabPanelBaseAppearance implements TabPanelAppearance {

  public interface ItemTemplate extends XTemplates {
    @XTemplate(source = "TabItem.html")
    SafeHtml render(TabPanelStyle style, TabItemConfig config);
  }

  public interface TabPanelResources {

    TabPanelStyle style();

  }

  public interface TabPanelStyle extends CssResource {

    String tab();

    String tabBbar();

    String tabBbarNoborder();

    String tabBody();

    String tabBodyBottom();

    String tabBodyNoborder();

    String tabBodyTop();

    String tabBwrap();

    String tabEdge();

    String tabFooter();

    String tabFooterNoborder();

    String tabHeader();

    String tabHeaderNoborder();

    String tabImage();

    String tabLeft();

    String tabNoborder();

    String tabRight();

    String tabScrollerLeft();

    String tabScrollerLeftDisabled();

    String tabScrollerLeftOver();

    String tabScrollerRight();

    String tabScrollerRightDisabled();

    String tabScrollerRightOver();

    String tabScrolling();

    String tabScrollingBottom();

    String tabsText();

    String tabStrip();

    String tabStripActive();

    String tabStripBottom();

    String tabStripClosable();

    String tabStripClose();

    String tabStripDisabled();

    String tabStripInner();

    String tabStripOver();

    String tabStripText();

    String tabStripTop();

    String tabStripWrap();

    String tabTbar();

    String tabTbarNoborder();

    String tabWithIcon();

    String xToolbar();

  }

  public interface Template extends XTemplates {
    @XTemplate(source = "TabPanel.html")
    SafeHtml render(TabPanelStyle style);
  }

  protected ItemTemplate itemTemplate;
  protected final TabPanelStyle style;
  protected Template template;

  private static final String ITEM_SELECTOR = "li";

  public TabPanelBaseAppearance(TabPanelResources resources, Template template, ItemTemplate itemTemplate) {
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);

    this.template = template;
    this.itemTemplate = itemTemplate;
  }

  @Override
  public void createScrollers(XElement parent) {
    int h = getStripWrap(parent).getOffsetHeight();
    SafeHtml html = SafeHtmlUtils.fromTrustedString("<div class='" + style.tabScrollerLeft() + "'></div>");
    XElement scrollLeft = getBar(parent).insertFirst(html);
    scrollLeft.setId(XDOM.getUniqueId());
    scrollLeft.setHeight(h);

    html = SafeHtmlUtils.fromTrustedString("<div class='" + style.tabScrollerRight() + "'></div>");
    XElement scrollRight = getBar(parent).insertFirst(html);
    scrollRight.setId(XDOM.getUniqueId());
    scrollRight.setHeight(h);
  }

  @Override
  public XElement getBar(XElement parent) {
    return parent.getFirstChildElement().cast();
  }

  @Override
  public XElement getBody(XElement parent) {
    return parent.getLastChild().cast();
  }

  @Override
  public String getItemSelector() {
    return ITEM_SELECTOR;
  }

  @Override
  public XElement getScrollLeft(XElement parent) {
    return getBar(parent).selectNode("." + style.tabScrollerLeft());
  }

  @Override
  public XElement getScrollRight(XElement parent) {
    return getBar(parent).selectNode("." + style.tabScrollerRight());
  }

  public XElement getStrip(XElement parent) {
    return getBar(parent).selectNode("." + style.tabStrip());
  }

  @Override
  public XElement getStripEdge(XElement parent) {
    return getBar(parent).selectNode("." + style.tabEdge());
  }

  @Override
  public XElement getStripWrap(XElement parent) {
    return getBar(parent).selectNode("." + style.tabStripWrap());
  }

  @Override
  public void insert(XElement parent, TabItemConfig config, int index) {
    XElement item = XDOM.create(itemTemplate.render(style, config));
    item.setClassName(ThemeStyles.get().style().disabled(), !config.isEnabled());
    
    getStrip(parent).insertChild(item, index);

    if (config.getIcon() != null) {
      setItemIcon(item, config.getIcon());
    }

    if (config.isClosable()) {
      item.addClassName(style.tabStripClosable());
    }
  }

  @Override
  public boolean isClose(XElement target) {
    return target.is("." + style.tabStripClose());
  }

  @Override
  public void onDeselect(Element item) {
    item.removeClassName(style.tabStripActive());
  }

  @Override
  public void onMouseOut(XElement parent, XElement target) {
    NodeList<Element> nodeList = parent.select("." + style.tabStripOver());
    for (int i = 0; i < nodeList.getLength(); i++) {
      nodeList.getItem(i).removeClassName(style.tabStripOver());
    }
    if (target.is("." + style.tabScrollerLeft())) {
      target.removeClassName(style.tabScrollerLeftOver());
    } else if (target.is("." + style.tabScrollerRight())) {
      target.removeClassName(style.tabScrollerRightOver());
    }
  }

  @Override
  public void onMouseOver(XElement parent, XElement target) {
    Element item = findItem(target);
    if (item != null) {
      item.addClassName(style.tabStripOver());
    } else if (target.is("." + style.tabScrollerLeft())) {
      target.addClassName(style.tabScrollerLeftOver());
    } else if (target.is("." + style.tabScrollerRight())) {
      target.addClassName(style.tabScrollerRightOver());
    }
  }

  @Override
  public void onScrolling(XElement parent, boolean scrolling) {
    parent.selectNode("." + style.tabHeader()).setClassName(style.tabScrolling(), scrolling);
  }

  @Override
  public void onSelect(Element item) {
    item.addClassName(style.tabStripActive());
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.render(style));
  }

  @Override
  public void setItemWidth(XElement element, int width) {
    XElement inner = element.selectNode("." + style.tabStripInner());
    int tw = element.getOffsetWidth();
    int iw = inner.getOffsetWidth();
    inner.setWidth(width - (tw - iw));
  }

  @Override
  public void updateItem(XElement item, TabItemConfig config) {
    XElement contentEl = item.selectNode("." + style.tabStripText());
    contentEl.setInnerSafeHtml(config.getContent());

    setItemIcon(item, config.getIcon());

    item.setClassName(ThemeStyles.get().style().disabled(), !config.isEnabled());

    item.setClassName(style.tabStripClosable(), config.isClosable());
  }

  @Override
  public void updateScrollButtons(XElement parent) {
    int pos = getScrollPos(parent);
    getScrollLeft(parent).setClassName(style.tabScrollerLeftDisabled(), pos == 0);
    getScrollRight(parent).setClassName(style.tabScrollerRightDisabled(),
        pos >= (getScrollWidth(parent) - getScrollArea(parent) - 2));
  }

  protected Element findItem(Element target) {
    return target.<XElement> cast().findParentElement(ITEM_SELECTOR, -1);
  }

  protected void setItemIcon(XElement item, ImageResource icon) {
    XElement node = item.selectNode("." + style.tabImage());
    if (node != null) {
      node.removeFromParent();
    }
    if (icon != null) {
      Element e = IconHelper.getElement(icon);
      e.setClassName(style.tabImage());
      item.appendChild(e);
    }
    item.setClassName(style.tabWithIcon(), icon != null);
  }

  private int getScrollPos(XElement parent) {
    return getStripWrap(parent).getScrollLeft();
  }

  private int getScrollArea(XElement parent) {
    return Math.max(0, getStripWrap(parent).getClientWidth());
  }

  private int getScrollWidth(XElement parent) {
    return getStripEdge(parent).getOffsetsTo(getStripWrap(parent)).getX() + getScrollPos(parent);
  }

}
