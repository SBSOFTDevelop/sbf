/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.status.BoxStatusBaseAppearance;
import ru.sbsoft.svc.widget.core.client.Status.BoxStatusAppearance;

public class BlueBoxStatusAppearance extends BoxStatusBaseAppearance implements BoxStatusAppearance {

  public interface BlueBoxStatusStyle extends BoxStatusStyle {

  }

  public interface BlueBoxStatusResources extends BoxStatusResources, ClientBundle {

    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/status/Status.gss", "BlueBoxStatus.gss"})
    BlueBoxStatusStyle style();
  }

  public BlueBoxStatusAppearance() {
    this(GWT.<BlueBoxStatusResources> create(BlueBoxStatusResources.class), GWT.<BoxTemplate> create(BoxTemplate.class));
  }

  public BlueBoxStatusAppearance(BlueBoxStatusResources resources, BoxTemplate template) {
    super(resources, template);
  }

}
