/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared;

/**
 * A concrete <code>LabelProvider</code> implementation that simple calls
 * toString on the target object.
 * 
 * @param <T> the target object type
 */
public class StringLabelProvider<T> implements LabelProvider<T> {

  @Override
  public String getLabel(T item) {
    return item.toString();
  }

}
