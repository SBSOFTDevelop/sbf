/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.statusproxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.statusproxy.StatusProxyBaseAppearance;

public class GrayStatusProxyAppearance extends StatusProxyBaseAppearance {

  public interface GrayStatusProxyResources extends StatusProxyResources, ClientBundle {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/statusproxy/StatusProxy.gss", "GrayStatusProxy.gss"})
    GrayStatusProxyStyle style();

  }

  public interface GrayStatusProxyStyle extends StatusProxyStyle {
  }

  public GrayStatusProxyAppearance() {
    this(GWT.<GrayStatusProxyResources> create(GrayStatusProxyResources.class),
        GWT.<StatusProxyTemplates> create(StatusProxyTemplates.class));
  }

  public GrayStatusProxyAppearance(GrayStatusProxyResources resources, StatusProxyTemplates templates) {
    super(resources, templates);
  }

}
