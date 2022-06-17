/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client;

import ru.sbsoft.svc.data.shared.PropertyAccess;

/**
 * Returns the property value of the target object.
 * 
 * @see PropertyAccess
 * @param <T> the target object type
 * @param <V> the property type
 */
public interface ValueProvider<T, V> {

  /**
   * Returns the property value of the given object.
   * 
   * @param object the target object
   * @return the property value
   */
  V getValue(T object);

  /**
   * Sets the value of the given object
   * 
   * @param object
   * @param value
   */
  void setValue(T object, V value);

  /**
   * Returns the path that this ValueProvider makes available, from the object,
   * to the value.
   * 
   * @return the path from the object to the value
   */
  String getPath();

}
