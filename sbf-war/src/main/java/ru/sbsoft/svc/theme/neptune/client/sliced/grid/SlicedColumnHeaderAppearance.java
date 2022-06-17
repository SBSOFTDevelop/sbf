/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.grid.Css3ColumnHeaderAppearance;

public class SlicedColumnHeaderAppearance extends Css3ColumnHeaderAppearance {

  public interface SlicedColumnHeaderResources extends Css3ColumnHeaderResources {
    /** header background image */
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("column-header.png")
    ImageResource columnHeader();

    /** header background image when hovered */
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("column-header-over.png")
    ImageResource columnHeaderOver();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("column-header-btn-bg.png")
    ImageResource columnHeaderBtnBg();
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("column-header-btn-bg-over.png")
    ImageResource columnHeaderBtnBgOver();
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("column-header-btn-bg-open.png")
    ImageResource columnHeaderBtnBgOpen();

    @Override
    @Source("SlicedColumnHeader.gss")
    Styles style();

  }

  public interface Styles extends Css3ColumnHeaderAppearance.Styles {

  }

  public SlicedColumnHeaderAppearance() {
    this(GWT.<SlicedColumnHeaderResources>create(SlicedColumnHeaderResources.class));
  }

  public SlicedColumnHeaderAppearance(SlicedColumnHeaderResources resources) {
    super(resources);
  }

}
