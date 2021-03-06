/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;

public class BlueHeaderAppearance extends HeaderDefaultAppearance {

  public interface BlueHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface BlueHeaderResources extends HeaderResources {

    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "BlueHeader.gss"})
    BlueHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }
  

  public BlueHeaderAppearance() {
    this(GWT.<BlueHeaderResources> create(BlueHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public BlueHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}
