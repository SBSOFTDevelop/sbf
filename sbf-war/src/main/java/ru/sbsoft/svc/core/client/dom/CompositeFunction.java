/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom;

import com.google.gwt.dom.client.Element;

/**
 * Interface for functions that can be applied to all the elements of a
 * <code>CompositeElement</code>.
 */
public interface CompositeFunction {

  /**
   * Called for each element in the composite element.
   * 
   * @param elem the child element
   * @param ce the composite element
   * @param index the child index
   */
  public void doFunction(Element elem, CompositeElement ce, int index);

}
