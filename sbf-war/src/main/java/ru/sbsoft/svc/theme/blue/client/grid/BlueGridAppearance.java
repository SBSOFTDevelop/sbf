/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource.Import;
import ru.sbsoft.svc.theme.base.client.grid.GridBaseAppearance;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStateStyles;

public class BlueGridAppearance extends GridBaseAppearance {

  public interface BlueGridStyle extends GridStyle {
    
  }
  
  public interface BlueGridResources extends GridResources  {

    @Import(GridStateStyles.class)
    @Source({"ru/sbsoft/svc/theme/base/client/grid/Grid.gss", "BlueGrid.gss"})
    @Override
    BlueGridStyle css();
  }
  
  
  public BlueGridAppearance() {
    this(GWT.<BlueGridResources> create(BlueGridResources.class));
  }

  public BlueGridAppearance(BlueGridResources resources) {
    super(resources);
  }
}
