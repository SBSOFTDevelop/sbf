/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.Import;
import ru.sbsoft.svc.theme.base.client.grid.GridBaseAppearance;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStateStyles;

public class GrayGridAppearance extends GridBaseAppearance {

  public interface GrayGridStyle extends GridStyle {
    
  }
  
  public interface GrayGridResources extends GridResources  {

    @Import(GridStateStyles.class)
    @Source({"ru/sbsoft/svc/theme/base/client/grid/Grid.gss", "GrayGrid.gss"})
    @Override
    GrayGridStyle css();
  }
  
  
  public GrayGridAppearance() {
    this(GWT.<GrayGridResources> create(GrayGridResources.class));
  }

  public GrayGridAppearance(GrayGridResources resources) {
    super(resources);
  }
}
