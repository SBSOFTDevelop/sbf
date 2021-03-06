/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class GrayHeaderAppearance extends HeaderDefaultAppearance {

  public interface GrayHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface GrayHeaderResources extends HeaderResources {

    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "GrayHeader.gss"})
    GrayHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }
  

  public GrayHeaderAppearance() {
    this(GWT.<GrayHeaderResources> create(GrayHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public GrayHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}
