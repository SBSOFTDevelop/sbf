/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.field;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.widget.core.client.form.error.SideErrorHandler.SideErrorResources;

public class Css3SideErrorResourcesAdapter implements SideErrorResources {

  interface Css3SideErrorResources extends SideErrorResources {
    @Override
    @Source("exclamation.png")
    ImageResource errorIcon();
  }

  private final Css3SideErrorResources resources;

  public Css3SideErrorResourcesAdapter() {
    this.resources = GWT.create(Css3SideErrorResources.class);
  }

  public ImageResource errorIcon() {
    return resources.errorIcon();
  }
}
