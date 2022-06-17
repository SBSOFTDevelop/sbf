/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.menu;

import com.google.gwt.resources.client.CssResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.menu.Item;

public abstract class ItemBaseAppearance implements Item.ItemAppearance {

  public interface ItemResources {

    ItemStyle style();

  }

  public interface ItemStyle extends CssResource {

    String active();

  }

  private final ItemStyle style;

  public ItemBaseAppearance(ItemResources resources) {
    style = resources.style();
    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  public void onActivate(XElement parent) {
    parent.addClassName(style.active());
  }

  public void onDeactivate(XElement parent) {
    parent.removeClassName(style.active());
  }

}
