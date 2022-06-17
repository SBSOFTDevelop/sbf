/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.button;

import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;

/**
 * A horizontal row of buttons.
 */
public class ButtonBar extends ToolBar {

  /**
   * Creates a left aligned button bar.
   */
  public ButtonBar() {
    super();
    setSpacing(5);
    removeStyleName("x-toolbar-mark");
  }

}
