/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.colorpalette;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.colorpalette.ColorPaletteBaseAppearance;

public class GrayColorPaletteAppearance extends ColorPaletteBaseAppearance {

  public interface GrayColorPaletteResources extends ColorPaletteBaseAppearance.ColorPaletteResources, ClientBundle {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/colorpalette/ColorPalette.gss", "GrayColorPalette.gss"})
    GrayColorPaletteStyle style();

  }

  public interface GrayColorPaletteStyle extends ColorPaletteStyle {
  }

  public GrayColorPaletteAppearance() {
    this(GWT.<GrayColorPaletteResources> create(GrayColorPaletteResources.class),
        GWT.<BaseColorPaletteTemplate> create(BaseColorPaletteTemplate.class));
  }

  public GrayColorPaletteAppearance(GrayColorPaletteResources resources, BaseColorPaletteTemplate template) {
    super(resources, template);
  }

}
