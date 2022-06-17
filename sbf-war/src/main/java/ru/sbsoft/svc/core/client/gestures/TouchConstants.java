/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * TODO make up a generator that reads from config-properties instead
 */
public interface TouchConstants extends Constants {
  public static final TouchConstants INSTANCE = GWT.create(TouchConstants.class);

  @DefaultIntValue(500)
  int longPressMs();

  @DefaultIntValue(15)
  int touchMaxDistance();
}
