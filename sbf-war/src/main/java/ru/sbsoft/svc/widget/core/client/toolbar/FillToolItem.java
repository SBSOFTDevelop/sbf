/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.toolbar;

import com.google.gwt.dom.client.Document;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;

/**
 * Fills the toolbar width, pushing any newly added items to the right.
 */
public class FillToolItem extends Component {

  /**
   * Creates a new fill item.
   */
  public FillToolItem() {
    setElement(Document.get().createDivElement());
    
    BoxLayoutData data = new BoxLayoutData();
    data.setFlex(1.0);
    setLayoutData(data);
  }

}
