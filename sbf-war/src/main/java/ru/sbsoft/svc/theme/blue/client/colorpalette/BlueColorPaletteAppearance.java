/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.colorpalette;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.colorpalette.ColorPaletteBaseAppearance;

public class BlueColorPaletteAppearance extends ColorPaletteBaseAppearance {

  public interface BlueColorPaletteResources extends ColorPaletteBaseAppearance.ColorPaletteResources, ClientBundle {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/colorpalette/ColorPalette.gss", "BlueColorPalette.gss"})
    BlueColorPaletteStyle style();

  }

  public interface BlueColorPaletteStyle extends ColorPaletteStyle {
  }

  public BlueColorPaletteAppearance() {
    this(GWT.<BlueColorPaletteResources> create(BlueColorPaletteResources.class),
        GWT.<BaseColorPaletteTemplate> create(BaseColorPaletteTemplate.class));
  }

  public BlueColorPaletteAppearance(BlueColorPaletteResources resources, BaseColorPaletteTemplate template) {
    super(resources, template);
  }

}
