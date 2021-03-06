/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.menu;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.menu.CheckMenuItem.CheckMenuItemAppearance;

public abstract class CheckMenuItemBaseAppearance extends MenuItemBaseAppearance implements CheckMenuItemAppearance {

  public interface CheckMenuItemResources extends MenuItemResources {

    CheckMenuItemStyle checkStyle();
    
    ImageResource checked();
    
    ImageResource unchecked();
    
    ImageResource groupChecked();

  }

  public interface CheckMenuItemStyle extends CssResource {

    String menuItemChecked();

  }

  private final CheckMenuItemResources resources;
  private final CheckMenuItemStyle checkStyle;

  public CheckMenuItemBaseAppearance(CheckMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
    this.resources = resources;
    checkStyle = resources.checkStyle();
  }

  @Override
  public void applyChecked(XElement parent, boolean state) {
    parent.setClassName(checkStyle.menuItemChecked(), state);
  }

  @Override
  public XElement getCheckIcon(XElement parent) {
    return parent.<XElement>cast().selectNode("." + resources.style().menuItemIcon());
  }

  @Override
  public ImageResource checked() {
    return resources.checked();
  }

  @Override
  public ImageResource unchecked() {
    return resources.unchecked();
  }

  @Override
  public ImageResource radio() {
    return resources.groupChecked();
  }

}
