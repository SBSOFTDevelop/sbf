/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.theme.neptune.client.base.menu.Css3CheckMenuItemAppearance.Css3CheckMenuItemResources;
import ru.sbsoft.svc.widget.core.client.menu.CheckMenuItem.CheckMenuItemAppearance;

public class SlicedCheckMenuItemAppearance extends SlicedMenuItemAppearance implements CheckMenuItemAppearance {

  private final Css3CheckMenuItemResources resources;

  public SlicedCheckMenuItemAppearance() {
    this(GWT.<Css3CheckMenuItemResources>create(Css3CheckMenuItemResources.class));
  }

  public SlicedCheckMenuItemAppearance(Css3CheckMenuItemResources resources) {
    this.resources = resources;
  }

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
}
