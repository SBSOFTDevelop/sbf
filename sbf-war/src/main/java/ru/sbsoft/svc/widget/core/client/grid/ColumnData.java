/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.safecss.shared.SafeStyles;

public class ColumnData {

  /**
   * The column styles.
   */
  private SafeStyles styles;

  /**
   * The column css.
   */
  private String css;

  public String getClassNames() {
    return css;
  }

  public SafeStyles getStyles() {
    return styles;
  }

  public void setClassNames(String css) {
    this.css = css;
  }

  public void setStyles(SafeStyles styles) {
    this.styles = styles;
  }

}
