/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.colorpalette;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.colorpalette.ColorPaletteBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3ColorPaletteAppearance extends ColorPaletteBaseAppearance {
  public interface Css3ColorPaletteResources extends ColorPaletteResources, ClientBundle {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/colorpalette/ColorPalette.gss", "Css3ColorPalette.gss"})
    Css3ColorPaletteStyle style();

    ThemeDetails theme();
  }

  public interface Css3ColorPaletteStyle extends ColorPaletteStyle {

  }

  public Css3ColorPaletteAppearance() {
    this(GWT.<Css3ColorPaletteResources>create(Css3ColorPaletteResources.class));
  }

  public Css3ColorPaletteAppearance(Css3ColorPaletteResources resources) {
    this(resources, GWT.<BaseColorPaletteTemplate>create(BaseColorPaletteTemplate.class));
  }

  public Css3ColorPaletteAppearance(ColorPaletteResources resources, BaseColorPaletteTemplate template) {
    super(resources, template);
  }
}
