/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.frame;

import ru.sbsoft.svc.core.client.dom.XElement;

/**
 * Interface for frames that can be expanded / collapsed.
 */
public interface CollapsibleFrame extends Frame {

  /**
   * Returns the element who's visibility will be "toggled" for expanding and
   * collapsing.
   * 
   * @param parent the parent element
   * @return the collapse target element
   */
  XElement getCollapseElem(XElement parent);
}
