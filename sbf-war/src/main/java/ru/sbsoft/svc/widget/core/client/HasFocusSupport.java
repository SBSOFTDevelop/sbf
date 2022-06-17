/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

/**
 * Implemented by objects that have focus support. It provides access to
 * {@link FocusManagerSupport}, which defines the action taken on tab and back
 * tab and allows components such as containers to be ignored when identifying
 * the next component to receive focus.
 */
public interface HasFocusSupport {
  /**
   * Returns the focus manager support, which defines the action taken on tab
   * and back tab and allows components such as containers to be ignored when
   * identifying the next component to receive focus.
   * 
   * @return the focus manager support
   */
  FocusManagerSupport getFocusSupport();
}
