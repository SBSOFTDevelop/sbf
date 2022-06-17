/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.toolbar;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.theme.neptune.client.base.container.Css3HBoxLayoutContainerAppearance;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar.ToolBarAppearance;

/**
 *
 */
public class Css3ToolBarAppearance extends Css3HBoxLayoutContainerAppearance implements ToolBarAppearance {

  public interface Css3ToolBarResources extends Css3HBoxLayoutContainerResources {
    @Source({"ru/sbsoft/svc/theme/base/client/container/BoxLayout.gss", "ru/sbsoft/svc/theme/neptune/client/base/container/Css3HBoxLayoutContainer.gss", "Css3ToolBar.gss"})
    Css3ToolBarStyle style();

    ThemeDetails theme();
  }

  public interface Css3ToolBarStyle extends Css3HBoxLayoutContainerStyle {
    String toolBar();
  }

  @Override
  public String toolBarClassName() {
    return style.toolBar();
  }


  private final Css3ToolBarResources resources;
  private final Css3ToolBarStyle style;

  public Css3ToolBarAppearance() {
    this(GWT.<Css3ToolBarResources>create(Css3ToolBarResources.class));
  }

  public Css3ToolBarAppearance(Css3ToolBarResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }
}
