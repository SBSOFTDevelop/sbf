/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client;

import ru.sbsoft.svc.core.client.dom.XElement;

public interface FocusableCell {

  XElement getFocusElement(XElement parent);
  
}
