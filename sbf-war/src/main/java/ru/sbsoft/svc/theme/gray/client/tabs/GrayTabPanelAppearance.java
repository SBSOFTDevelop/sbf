/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.tabs;

import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Both;
import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Horizontal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import ru.sbsoft.svc.theme.base.client.tabs.TabPanelBaseAppearance;

public class GrayTabPanelAppearance extends TabPanelBaseAppearance {

  public interface GrayTabPanelResources extends TabPanelResources, ClientBundle {

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomInactiveLeftBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomInactiveRightBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomLeftBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomRightBackground();

    ImageResource scrollerLeft();

    ImageResource scrollerLeftOver();

    ImageResource scrollerRight();

    ImageResource scrollerRightOver();

    @Source({"ru/sbsoft/svc/theme/base/client/tabs/TabPanel.gss", "GrayTabPanel.gss"})
    GrayTabPanelStyle style();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenter();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenterActive();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenterOver();

    ImageResource tabClose();

    ImageResource tabLeft();

    ImageResource tabLeftActive();

    ImageResource tabLeftOver();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRight();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRightActive();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRightOver();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabStripBackground();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabStripBottomBackground();

  }

  public interface GrayTabPanelStyle extends TabPanelStyle {
  }

  public GrayTabPanelAppearance() {
    this(GWT.<GrayTabPanelResources> create(GrayTabPanelResources.class), GWT.<Template> create(Template.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public GrayTabPanelAppearance(GrayTabPanelResources resources, Template template, ItemTemplate itemTemplate) {
    super(resources, template, itemTemplate);
  }

}
