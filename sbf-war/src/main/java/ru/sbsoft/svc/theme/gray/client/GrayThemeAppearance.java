/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.resources.ThemeStyles.Styles;
import ru.sbsoft.svc.core.client.resources.ThemeStyles.ThemeAppearance;

public class GrayThemeAppearance implements ThemeAppearance {

  static interface Bundle extends ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/BaseTheme.gss", "GrayTheme.gss"})
    GrayStyles css();
  }

  interface GrayStyles extends Styles {
    String borderColor();

    String borderColorLight();

    String backgroundColorLight();
  }

  private Bundle bundle;
  private GrayStyles style;

  @Override
  public Styles style() {
    return style;
  }

  public GrayThemeAppearance() {
    this.bundle = GWT.create(Bundle.class);
    this.style = bundle.css();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public String borderColor() {
    return style.borderColor();
  }

  @Override
  public String borderColorLight() {
    return style.borderColorLight();
  }

  @Override
  public String backgroundColorLight() {
    return style.backgroundColorLight();
  }
}
