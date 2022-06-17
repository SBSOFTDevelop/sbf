/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public class BlueHeaderFramedAppearance extends BlueHeaderAppearance {

  public interface BlueHeaderFramedStyle extends HeaderStyle {

  }

  public interface BlueFramedHeaderResources extends HeaderResources {

    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "BlueHeader.gss", "BlueFramedHeader.gss"})
    BlueHeaderFramedStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }

  public BlueHeaderFramedAppearance() {
    this(GWT.<BlueFramedHeaderResources> create(BlueFramedHeaderResources.class), GWT.<Template> create(Template.class));
  }

  public BlueHeaderFramedAppearance(BlueFramedHeaderResources resources, Template template) {
    super(resources, template);
  }
}
