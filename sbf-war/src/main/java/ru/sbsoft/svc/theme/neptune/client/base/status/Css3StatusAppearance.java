/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.status;

import com.google.gwt.core.shared.GWT;
import ru.sbsoft.svc.theme.base.client.status.StatusDefaultAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3StatusAppearance extends StatusDefaultAppearance {
  public interface Css3StatusResources extends StatusResources {
    @Override
    @Source("Css3Status.gss")
    Css3StatusStyles style();

    ThemeDetails theme();
  }

  public interface Css3StatusStyles extends StatusStyle {

  }

  public Css3StatusAppearance() {
    this(GWT.<Css3StatusResources>create(Css3StatusResources.class));
  }

  public Css3StatusAppearance(StatusResources resources) {
    this(resources, GWT.<Template>create(Template.class));
  }

  public Css3StatusAppearance(StatusResources resources, Template template) {
    super(resources, template);
  }
}
