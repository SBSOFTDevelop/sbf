/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.loader;

import java.util.Date;

/**
 * A {@link FilterHandler} that provides support for <code>Date</code> values.
 */
public class DateFilterHandler extends FilterHandler<Date> {
  @Override
  public Date convertToObject(String value) {
    return new Date(Long.parseLong(value));
  }

  @Override
  public String convertToString(Date object) {
    return "" + object.getTime();
  }
}
