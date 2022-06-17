/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import ru.sbsoft.svc.core.client.util.Margins;

/**
 * Layout parameter that specifies a widget's margins.
 */
public class MarginData implements HasMargins {
  private Margins margins;

  /**
   * Creates a layout parameter that specifies a widget's margins.
   */
  public MarginData() {
  }

  /**
   * Creates a layout parameter with the specified margins.
   * 
   * @param margins the margins
   */
  public MarginData(int margins) {
    this.margins = new Margins(margins);
  }

  /**
   * Creates a layout parameter with the specified margins.
   * 
   * @param top the top margin
   * @param right the right margin
   * @param bottom the bottom margin
   * @param left the left margin
   */
  public MarginData(int top, int right, int bottom, int left) {
    this.margins = new Margins(top, right, bottom, left);
  }

  /**
   * Creates a layout parameter with the specified margins.
   * 
   * @param margins the margins
   */
  public MarginData(Margins margins) {
    this.margins = margins;
  }

  @Override
  public Margins getMargins() {
    return margins;
  }

  @Override
  public void setMargins(Margins margins) {
    this.margins = margins;

  }

}
