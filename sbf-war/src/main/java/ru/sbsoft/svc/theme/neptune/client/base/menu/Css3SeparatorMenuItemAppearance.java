/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.menu;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.menu.SeparatorMenuItemBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3SeparatorMenuItemAppearance extends SeparatorMenuItemBaseAppearance {

  public interface Css3SeparatorMenuItemResources extends SeparatorMenuItemResources, ClientBundle {
    @Override
    @Source("Css3SeparatorMenuItem.gss")
    Css3SeparatorMenuItemStyle style();

    ThemeDetails theme();
  }

  public interface Css3SeparatorMenuItemStyle extends SeparatorMenuItemStyle {

  }

  public Css3SeparatorMenuItemAppearance() {
    this(GWT.<Css3SeparatorMenuItemResources>create(Css3SeparatorMenuItemResources.class));
  }

  public Css3SeparatorMenuItemAppearance(Css3SeparatorMenuItemResources resources) {
    this(resources, GWT.<SeparatorMenuItemTemplate>create(SeparatorMenuItemTemplate.class));
  }

  public Css3SeparatorMenuItemAppearance(Css3SeparatorMenuItemResources resources, SeparatorMenuItemTemplate template) {
    super(resources, template);
  }
}
