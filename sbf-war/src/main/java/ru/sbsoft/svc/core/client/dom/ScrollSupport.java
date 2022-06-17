/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom;

import com.google.gwt.user.client.ui.UIObject;

/**
 * Interface for objects that support scrolling.
 */
public interface ScrollSupport {

  /**
   * Scroll enumeration.
   */
  public enum ScrollMode {
    AUTO("auto"), AUTOX("auto"), AUTOY("auto"), ALWAYS("scroll"), NONE("hidden");
    private final String value;

    private ScrollMode(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }
  }
  
  public void ensureVisible(UIObject item);
  
  public int getHorizontalScrollPosition();

  public int getMaximumHorizontalScrollPosition();

  public int getMaximumVerticalScrollPosition();

  public int getMinimumHorizontalScrollPosition();

  public int getMinimumVerticalScrollPosition();

  public ScrollMode getScrollMode();

  public int getVerticalScrollPosition();

  public void scrollToBottom();

  public void scrollToLeft();

  public void scrollToRight();

  public void scrollToTop();

  public void setHorizontalScrollPosition(int position);

  public void setScrollMode(ScrollMode scroll);

  public void setVerticalScrollPosition(int position);

}
