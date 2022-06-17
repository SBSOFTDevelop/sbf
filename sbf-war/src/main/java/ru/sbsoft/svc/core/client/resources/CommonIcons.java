/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class CommonIcons {

  public static interface Icons extends ClientBundle {

    ImageResource lessThan();
    
    ImageResource greaterThan();
    
    ImageResource equals();

  }

  private static final Icons instance = GWT.create(Icons.class);

  public static Icons get() {
    return instance;
  }

}
