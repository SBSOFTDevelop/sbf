/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.listview;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.neptune.client.base.listview.Css3ListViewAppearance;

public class SlicedListViewAppearance<M> extends Css3ListViewAppearance<M> {

  public interface SlicedListViewResources extends Css3ListViewResources {
    @Override
    @Source("SlicedListView.gss")
    SlicedListViewStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("item.png")
    ImageResource itemBackground();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("itemselected.png")
    ImageResource itemSelectedBackground();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("itemover.png")
    ImageResource itemOverBackground();
  }

  public interface SlicedListViewStyle extends Css3ListViewStyle {

  }

  public SlicedListViewAppearance() {
    this(GWT.<SlicedListViewResources>create(SlicedListViewResources.class));
  }

  public SlicedListViewAppearance(SlicedListViewResources resources) {
    super(resources);
  }
}
