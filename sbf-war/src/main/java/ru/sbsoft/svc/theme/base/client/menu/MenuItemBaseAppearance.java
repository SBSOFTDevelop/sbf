/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.menu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.IconHelper;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.widget.core.client.menu.MenuItem;

public abstract class MenuItemBaseAppearance extends ItemBaseAppearance implements MenuItem.MenuItemAppearance {

  public interface MenuItemResources extends ItemResources {
    @Override
    MenuItemStyle style();
  }

  public interface MenuItemStyle extends ItemStyle {

    String menuItem();

    String menuItemArrow();

    String menuItemIcon();

    String menuListItem();

  }

  public interface MenuItemTemplate extends XTemplates {

    @XTemplates.XTemplate(source = "MenuItem.html")
    SafeHtml template(MenuItemStyle style);

  }

  private final MenuItemStyle style;
  private final MenuItemTemplate template;

  public MenuItemBaseAppearance(MenuItemResources resources, MenuItemTemplate template) {
    super(resources);
    style = resources.style();
    this.template = template;
  }

  public XElement getAnchor(XElement parent) {
    return XElement.as(parent.getFirstChild());
  }

  public void onAddSubMenu(XElement parent) {
    parent.getFirstChildElement().addClassName(style.menuItemArrow());
  }

  public void onRemoveSubMenu(XElement parent) {
    parent.getFirstChildElement().removeClassName(style.menuItemArrow());
  }

  public void render(SafeHtmlBuilder result) {
    result.append(template.template(style));
  }

  public void setIcon(XElement parent, ImageResource icon) {
    XElement anchor = getAnchor(parent);
    XElement oldIcon = parent.selectNode("." + style.menuItemIcon());
    if (oldIcon != null) {
      oldIcon.removeFromParent();
    }
    if (icon != null) {
      Element e = IconHelper.getElement(icon);
      e.addClassName(style.menuItemIcon());
      anchor.insertChild(e, 0);
    }
  }

  @Override
  public void setHtml(XElement parent, SafeHtml html) {
    XElement oldIcon = parent.selectNode("." + style.menuItemIcon());

    if (html == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      getAnchor(parent).setInnerSafeHtml(Util.NBSP_SAFE_HTML);
    } else {
      getAnchor(parent).setInnerSafeHtml(html);
    }

    if (oldIcon != null) {
      getAnchor(parent).insertFirst(oldIcon);
    }
  }

  @Override
  public void setWidget(XElement parent, Widget widget) {
    XElement oldIcon = parent.selectNode("." + style.menuItemIcon());

    getAnchor(parent).setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    getAnchor(parent).appendChild(widget.getElement());

    if (oldIcon != null) {
      getAnchor(parent).insertFirst(oldIcon);
    }
  }

}
