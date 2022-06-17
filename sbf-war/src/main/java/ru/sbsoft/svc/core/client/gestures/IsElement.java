/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures;

import com.google.gwt.dom.client.Element;

/**
 * Wrap a GWT Element object, allowing non-GWT test code to
 */
public interface IsElement {
  Element asElement();
}
