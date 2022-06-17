/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.resources.ThemeStyles.Styles;
import ru.sbsoft.svc.core.client.resources.ThemeStyles.ThemeAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

/**
 *
 */
public class Css3ThemeAppearance implements ThemeAppearance {

  public interface Css3ThemeResources extends ClientBundle {
    @Source("Css3Theme.gss")
    Css3ThemeStyles style();

    ThemeDetails theme();
  }

  public interface Css3ThemeStyles extends Styles {

  }


  private final Css3ThemeStyles style;
  private final Css3ThemeResources resources;

  public Css3ThemeAppearance() {
    this(GWT.<Css3ThemeResources>create(Css3ThemeResources.class));
  }

  public Css3ThemeAppearance(Css3ThemeResources resources) {
    this.resources = resources;
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }


  @Override
  public Styles style() {
    return style;
  }

  @Override
  public String borderColor() {
    return resources.theme().borderColor();
  }

  @Override
  public String borderColorLight() {
    return resources.theme().borderColor();
  }

  @Override
  public String backgroundColorLight() {
    return resources.theme().backgroundColor();
  }
}
