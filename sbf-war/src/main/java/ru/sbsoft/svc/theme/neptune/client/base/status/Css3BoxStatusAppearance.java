/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.status;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.status.BoxStatusBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.Status.BoxStatusAppearance;

public class Css3BoxStatusAppearance extends BoxStatusBaseAppearance implements BoxStatusAppearance {

  public interface Css3BoxStatusResources extends BoxStatusResources, ClientBundle {
    @Override
    @Source("Css3BoxStatus.gss")
    Css3BoxStatusStyle style();

    ThemeDetails theme();
  }

  public interface Css3BoxStatusStyle extends BoxStatusStyle {

  }

  public Css3BoxStatusAppearance() {
    this(GWT.<Css3BoxStatusResources>create(Css3BoxStatusResources.class));
  }

  public Css3BoxStatusAppearance(BoxStatusResources resources) {
    this(resources, GWT.<BoxTemplate>create(BoxTemplate.class));
  }

  public Css3BoxStatusAppearance(BoxStatusResources resources, BoxTemplate template) {
    super(resources, template);
  }
}
