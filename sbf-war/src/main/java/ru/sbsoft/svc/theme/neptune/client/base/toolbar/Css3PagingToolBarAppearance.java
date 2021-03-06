/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.widget.core.client.toolbar.PagingToolBar.PagingToolBarAppearance;

/**
 *
 */
public class Css3PagingToolBarAppearance implements PagingToolBarAppearance {


  public interface Css3PagingToolBarResources extends PagingToolBarAppearance, ClientBundle {
    ImageResource first();

    ImageResource prev();

    ImageResource next();

    ImageResource last();

    ImageResource refresh();

    ImageResource loading();
  }

  private final Css3PagingToolBarResources resources;

  public Css3PagingToolBarAppearance() {
    this(GWT.<Css3PagingToolBarResources>create(Css3PagingToolBarResources.class));
  }

  public Css3PagingToolBarAppearance(Css3PagingToolBarResources resources) {
    this.resources = resources;
  }

  @Override
  public ImageResource first() {
    return resources.first();
  }

  @Override
  public ImageResource last() {
    return resources.last();
  }

  @Override
  public ImageResource next() {
    return resources.next();
  }

  @Override
  public ImageResource prev() {
    return resources.prev();
  }

  @Override
  public ImageResource refresh() {
    return resources.refresh();
  }

  @Override
  public ImageResource loading() {
    return resources.loading();
  }
}
