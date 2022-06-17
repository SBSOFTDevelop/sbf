/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;

public class TargetContext extends Context {

  protected Element parent;

  public TargetContext(Element parent, int index, int column, Object key) {
    super(index, column, key);
    this.parent = parent;
  }

  public Element getParent() {
    return parent;
  }

}
