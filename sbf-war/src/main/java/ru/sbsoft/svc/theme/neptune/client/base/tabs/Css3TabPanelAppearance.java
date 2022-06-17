/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.resources.client.ClientBundle;
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
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.TabItemConfig;
import ru.sbsoft.svc.widget.core.client.TabPanel.TabPanelAppearance;

/**
 *
 */
public class Css3TabPanelAppearance implements TabPanelAppearance {

  public interface ItemTemplate extends XTemplates {
    @XTemplate(source = "Css3TabPanelTabItem.html")
    SafeHtml render(Css3TabPanelStyle style, TabItemConfig config);
  }

  public interface Css3TabPanelResources extends ClientBundle {
    @Source("Css3TabPanel.gss")
    Css3TabPanelStyle style();

    ThemeDetails theme();

    ImageResource activeTabClose();

    ImageResource activeTabCloseOver();

    ImageResource tabClose();

    ImageResource tabCloseOver();

    ImageResource scrollerLeft();

    ImageResource scrollerRight();
  }

  public interface Css3TabPanelStyle extends CssResource {
    /** the entire tab panel */
    String tab();

    /** contents of the tab panel */
    String tabBody();

    String tabEdge();

    /** tab bar, when above the body */
    String tabBar();

    /** icon element inside a tab item */
    String tabImage();

    /** the tab item itself */
    String tabItem();

    String tabRight();

    String tabScrollerLeft();

    String tabScrollerLeftDisabled();

    String tabScrollerLeftOver();

    String tabScrollerRight();

    String tabScrollerRightDisabled();

    String tabScrollerRightOver();

    String tabScrolling();

    String tabStrip();

    /** marker on the tab item that it is active */
    String tabStripActive();

    /** marker on the tab item that it may be closed */
    String tabStripClosable();

    /** close icon within a tab item, may not be visible */
    String tabStripClose();

    String tabStripInner();

    /** marker on the tab item that it is hovered */
    String tabStripOver();

    /** text element inside the tab item */
    String tabStripText();

    String tabStripWrap();

    /** extra marker on the tab item to indicate that there is an icon */
    String tabWithIcon();
  }

  public interface Css3TabPanelTemplates extends XTemplates {
    @XTemplate(source = "Css3TabPanel.html")
    SafeHtml render(Css3TabPanelStyle style);
  }

  protected ItemTemplate itemTemplate = GWT.<ItemTemplate> create(ItemTemplate.class);
  protected final Css3TabPanelStyle style;
  protected Css3TabPanelTemplates template;

  private static final String ITEM_SELECTOR = "li";

  public Css3TabPanelAppearance() {
    this(GWT.<Css3TabPanelResources> create(Css3TabPanelResources.class));
  }

  public Css3TabPanelAppearance(Css3TabPanelResources resources) {
    this(resources, GWT.<Css3TabPanelTemplates> create(Css3TabPanelTemplates.class));
  }

  public Css3TabPanelAppearance(Css3TabPanelResources resources, Css3TabPanelTemplates template) {
    this.style = resources.style();
    this.template = template;

    StyleInjectorHelper.ensureInjected(this.style, true);
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
    return parent.selectNode("." + style.tabBody());
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
    parent.selectNode("." + style.tabBar()).setClassName(style.tabScrolling(), scrolling);
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
    element.setWidth(width, true);
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
