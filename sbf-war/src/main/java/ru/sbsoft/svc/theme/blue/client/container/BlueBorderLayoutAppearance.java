/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.container;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.container.BorderLayoutBaseAppearance;

public class BlueBorderLayoutAppearance extends BorderLayoutBaseAppearance {

  public interface BlueBorderLayoutResources extends BorderLayoutResources {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/container/BorderLayout.gss", "BlueBorderLayout.gss"})
    public BlueBorderLayoutStyle css();
  }

  public interface BlueBorderLayoutStyle extends BorderLayoutStyle {

  }

  public BlueBorderLayoutAppearance() {
    super(GWT.<BlueBorderLayoutResources> create(BlueBorderLayoutResources.class));
  }

}
