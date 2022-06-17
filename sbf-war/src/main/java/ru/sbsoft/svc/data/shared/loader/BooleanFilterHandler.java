/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

/**
 * A {@link FilterHandler} that provides support for <code>Boolean</code> values.
 */
public class BooleanFilterHandler extends FilterHandler<Boolean> {

  @Override
  public Boolean convertToObject(String value) {
    return Boolean.valueOf(value);
  }

  @Override
  public String convertToString(Boolean object) {
    return object.toString();
  }
}
