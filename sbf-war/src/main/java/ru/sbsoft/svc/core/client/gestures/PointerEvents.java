/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import ru.sbsoft.svc.core.client.SVC;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum PointerEvents {

  POINTERDOWN("MSPointerDown", "pointerdown"),
  POINTERUP("MSPointerUp", "pointerup"),
  POINTERCANCEL("MSPointerCancel", "pointercancel"),
  POINTERMOVE("MSPointerMove", "pointermove"),
  POINTEROVER("MSPointerOver", "pointerover"),
  POINTERENTER("MSPointerEnter", "pointerenter"),
  POINTERLEAVE("MSPointerLeave", "pointerleave");

  private static final Set<String> LOOKUP;
  static {
    Set<String> lookup = new HashSet<String>(values().length);
    for (PointerEvents pointerEvent : values()) {
      lookup.add(pointerEvent.eventName);
    }
    LOOKUP = Collections.unmodifiableSet(lookup);
  }

  private final String eventName;

  private PointerEvents(String msPrefixedEventName, String eventName) {
    this.eventName = SVC.isIE11() || SVC.isMSEdge() ? eventName : msPrefixedEventName;
  }

  public String getEventName() {
    return eventName;
  }

  public static boolean isPointerEvent(String eventType) {
    return LOOKUP.contains(eventType);
  }
}
