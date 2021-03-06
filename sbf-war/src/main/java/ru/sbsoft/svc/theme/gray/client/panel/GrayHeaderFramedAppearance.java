/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public class GrayHeaderFramedAppearance extends GrayHeaderAppearance {

  public interface GrayHeaderFramedStyle extends HeaderStyle {

  }

  public interface GrayFramedHeaderResources extends HeaderResources {

    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "GrayHeader.gss", "GrayFramedHeader.gss"})
    GrayHeaderFramedStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }

  public GrayHeaderFramedAppearance() {
    this(GWT.<GrayFramedHeaderResources> create(GrayFramedHeaderResources.class), GWT.<Template> create(Template.class));
  }

  public GrayHeaderFramedAppearance(GrayFramedHeaderResources resources, Template template) {
    super(resources, template);
  }
}
