/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import com.google.gwt.text.shared.Parser;

/**
 * A {@link FilterHandler} that provides support for <code>Number</code> values
 * and uses a property editor to convert a string representation to a number.
 */
public class NumberFilterHandler<V extends Number> extends FilterHandler<V> {

  /**
   * The {@link Parser} (probably a <code>NumberPropertyEditor</code>) this 
   * <code>NumberFilterHandler</code> uses to perform the conversion.
   */
  protected Parser<V> propertyEditor;

  /**
   * Creates a number filter handler that uses the given property editor to
   * convert a string representation to a number.
   * 
   * @param propertyEditor the property editor to use to do the conversion
   */
  public NumberFilterHandler(Parser<V> propertyEditor) {
    this.propertyEditor = propertyEditor;
  }

  @Override
  public V convertToObject(String value) {
    try {
      return propertyEditor.parse(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String convertToString(V object) {
    return object.toString();
  }
}
