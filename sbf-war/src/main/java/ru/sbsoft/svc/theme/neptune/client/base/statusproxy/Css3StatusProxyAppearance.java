/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.statusproxy;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.theme.base.client.statusproxy.StatusProxyBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3StatusProxyAppearance extends StatusProxyBaseAppearance {
  public interface Css3StatusProxyResources extends StatusProxyResources, ClientBundle {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/statusproxy/StatusProxy.gss", "Css3StatusProxy.gss"})
    Css3StatusProxyStyle style();

    @Override
    ImageResource dropAllowed();

    @Override
    ImageResource dropNotAllowed();

    ThemeDetails theme();
  }

  public interface Css3StatusProxyStyle extends StatusProxyStyle {

  }

  public Css3StatusProxyAppearance() {
    this(GWT.<Css3StatusProxyResources>create(Css3StatusProxyResources.class));
  }

  public Css3StatusProxyAppearance(Css3StatusProxyResources resources) {
    this(resources, GWT.<StatusProxyTemplates>create(StatusProxyTemplates.class));
  }

  public Css3StatusProxyAppearance(Css3StatusProxyResources resources, StatusProxyTemplates template) {
    super(resources, template);
  }
}
