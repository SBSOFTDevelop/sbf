/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Lists the available pointer events types
 */
public enum PointerType {

  MOUSE("mouse"), PEN("pen"), TOUCH("touch");

  private static final Map<String, PointerType> LOOKUP;

  static {
    Map<String, PointerType> lookup = new HashMap<String, PointerType>(values().length);
    for (PointerType type : values()) {
      lookup.put(type.type, type);
    }
    LOOKUP = Collections.unmodifiableMap(lookup);
  }

  private final String type;

  private PointerType(String type) {
    this.type = type;
  }

  /**
   * Returns enum value based type name
   *
   * @param type
   * @return PointerType
   */
  public static PointerType getPointerType(String type) {
    return LOOKUP.get(type);
  }
}
