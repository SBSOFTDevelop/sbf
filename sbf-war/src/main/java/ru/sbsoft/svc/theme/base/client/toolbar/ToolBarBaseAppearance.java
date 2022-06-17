/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.toolbar;

import ru.sbsoft.svc.theme.base.client.container.HBoxLayoutDefaultAppearance;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar.ToolBarAppearance;

public abstract class ToolBarBaseAppearance extends HBoxLayoutDefaultAppearance implements ToolBarAppearance {

  public interface ToolBarBaseStyle {
    String toolBar();

    String moreButton();
  }

}
