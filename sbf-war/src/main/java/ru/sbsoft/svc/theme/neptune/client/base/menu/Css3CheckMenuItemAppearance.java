/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.menu.CheckMenuItem.CheckMenuItemAppearance;

public class Css3CheckMenuItemAppearance extends Css3MenuItemAppearance implements CheckMenuItemAppearance {

  public interface Css3CheckMenuItemResources extends Css3MenuItemResources {
    ImageResource checked();

    ImageResource unchecked();

    ImageResource groupChecked();
  }

  private final Css3CheckMenuItemResources resources;

  @Override
  public void applyChecked(XElement parent, boolean state) {
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

  public Css3CheckMenuItemAppearance() {
    this(GWT.<Css3CheckMenuItemResources>create(Css3CheckMenuItemResources.class));
  }

  public Css3CheckMenuItemAppearance(Css3CheckMenuItemResources resources) {
    this(resources, GWT.<MenuItemTemplate>create(MenuItemTemplate.class));
  }

  public Css3CheckMenuItemAppearance(Css3CheckMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
    this.resources = resources;
  }
}
