/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.status.BoxStatusBaseAppearance;
import ru.sbsoft.svc.widget.core.client.Status.BoxStatusAppearance;

public class GrayBoxStatusAppearance extends BoxStatusBaseAppearance implements BoxStatusAppearance {

  public interface GrayBoxStatusStyle extends BoxStatusStyle {

  }

  public interface GrayBoxStatusResources extends BoxStatusResources, ClientBundle {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/status/Status.gss", "GrayBoxStatus.gss"})
    GrayBoxStatusStyle style();

  }

  public GrayBoxStatusAppearance() {
    this(GWT.<GrayBoxStatusResources> create(GrayBoxStatusResources.class), GWT.<BoxTemplate> create(BoxTemplate.class));
  }

  public GrayBoxStatusAppearance(GrayBoxStatusResources resources, BoxTemplate template) {
    super(resources, template);
  }

}
