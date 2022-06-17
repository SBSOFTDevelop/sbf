/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

/**
 * Abstract base class for all filter handlers. It provides methods to convert
 * filter values between native and string representations.
 * 
 * @param <T> the type of the filter value
 */
public abstract class FilterHandler<T> {

  /**
   * Converts a filter value in string representation to native representation.
   * 
   * @param value the string representation of the filter value
   * @return the native representation of the filter value
   */
  public abstract T convertToObject(String value);

  /**
   * Converts a filter value in native representation to string representation.
   * 
   * @param object the native representation of the filter value
   * @return the string representation of the filter value
   */
  public abstract String convertToString(T object);

}
