/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.container;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.container.BorderLayoutBaseAppearance;

public class GrayBorderLayoutAppearance extends BorderLayoutBaseAppearance {

  public interface GrayBorderLayoutResources extends BorderLayoutResources {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/container/BorderLayout.gss", "GrayBorderLayout.gss"})
    public GrayBorderLayoutStyle css();
  }

  public interface GrayBorderLayoutStyle extends BorderLayoutStyle {

  }

  public GrayBorderLayoutAppearance() {
    super(GWT.<GrayBorderLayoutResources> create(GrayBorderLayoutResources.class));
  }

}
