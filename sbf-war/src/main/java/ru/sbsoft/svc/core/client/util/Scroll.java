/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.util;

/**
 * Represents the left and top scroll values.
 */
public class Scroll {

  private int scrollLeft;
  private int scrollTop;

  public Scroll(int scrollLeft, int scrollTop) {
    this.scrollLeft = scrollLeft;
    this.scrollTop = scrollTop;
  }

  /**
   * Returns the scroll left value.
   * 
   * @return the scroll left value
   */
  public int getScrollLeft() {
    return scrollLeft;
  }

  /**
   * Returns the scroll top value.
   * 
   * @return the scroll top value
   */
  public int getScrollTop() {
    return scrollTop;
  }

  /**
   * Sets the scroll left value.
   * 
   * @param scrollLeft the scroll left value
   */
  public void setScrollLeft(int scrollLeft) {
    this.scrollLeft = scrollLeft;
  }

  /**
   * Sets the scroll top value.
   * 
   * @param scrollTop the scroll top value
   */
  public void setScrollTop(int scrollTop) {
    this.scrollTop = scrollTop;
  }
}
