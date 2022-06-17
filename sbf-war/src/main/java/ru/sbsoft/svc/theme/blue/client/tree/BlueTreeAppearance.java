/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import ru.sbsoft.svc.theme.base.client.tree.TreeBaseAppearance;

public class BlueTreeAppearance extends TreeBaseAppearance {

  public interface BlueTreeResources extends TreeResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/tree/Tree.gss", "TreeDefault.gss"})
    TreeBaseStyle style();

  }

  public BlueTreeAppearance() {
    super((TreeResources) GWT.create(BlueTreeResources.class));
  }
}
