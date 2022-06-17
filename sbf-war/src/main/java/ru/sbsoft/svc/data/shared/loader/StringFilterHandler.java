/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

/**
 * A {@link FilterHandler} that provides support for <code>String</code> values.
 */
public class StringFilterHandler extends FilterHandler<String> {

  @Override
  public String convertToObject(String value) {
    return value;
  }

  @Override
  public String convertToString(String object) {
    return object;
  }

}
