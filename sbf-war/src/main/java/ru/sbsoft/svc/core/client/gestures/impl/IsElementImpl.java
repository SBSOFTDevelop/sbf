/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.gestures.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import ru.sbsoft.svc.core.client.gestures.IsElement;

/**
 */
public class IsElementImpl extends JavaScriptObject implements IsElement {
  protected IsElementImpl() {
  }

  @Override
  public native final Element asElement() /*-{
    return this;
  }-*/;
}
