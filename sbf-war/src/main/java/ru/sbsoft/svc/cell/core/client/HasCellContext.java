/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import com.google.gwt.dom.client.Element;

public interface HasCellContext {

  TargetContext getContext(int row, int column);
  
  Element findTargetElement(int row, int column);
  
}
