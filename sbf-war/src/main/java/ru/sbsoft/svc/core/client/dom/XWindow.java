/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

public class XWindow {

  private static boolean lastResizeOrientationChange;

  static {
    Window.addResizeHandler(new ResizeHandler() {
      int lastWindowWidth;
      int lastWindowHeight;

      @Override
      public void onResize(ResizeEvent resizeEvent) {
        int width = resizeEvent.getWidth();
        int height = resizeEvent.getHeight();
        // check orientation change by seeing if height/width values swapped
        lastResizeOrientationChange = width == lastWindowHeight && height == lastWindowWidth;
        lastWindowWidth = width;
        lastWindowHeight = height;
      }
    });
  }

  private XWindow() {
  }

  /**
   * Returns true if the last resize was an orientation change. This is currently used by Android devices to determine if
   * the last window resize was caused by an orientation change or a virtual keyboard.
   *
   * @return true if the last resize was an orientation change.
   */
  public static boolean isLastResizeOrientationChange() {
    return lastResizeOrientationChange;
  }
}
