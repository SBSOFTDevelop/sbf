/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.panel;


import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.widget.HeaderDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

/**
 */
public class Css3HeaderAppearance extends HeaderDefaultAppearance {

  public interface Css3HeaderStyle extends HeaderStyle {
    @Override
    String header();

    @Override
    String headerBar();

    @Override
    String headerHasIcon();

    @Override
    String headerIcon();

    @Override
    String headerText();
  }

  public interface Css3HeaderResources extends HeaderResources {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/widget/Header.gss", "Css3Header.gss"})
    Css3HeaderStyle style();
    
    ThemeDetails theme();
  }
  
  public Css3HeaderAppearance() {
    this(GWT.<Css3HeaderResources>create(Css3HeaderResources.class));
  }

  public Css3HeaderAppearance(Css3HeaderResources resources) {
    super(resources);
  }
}
