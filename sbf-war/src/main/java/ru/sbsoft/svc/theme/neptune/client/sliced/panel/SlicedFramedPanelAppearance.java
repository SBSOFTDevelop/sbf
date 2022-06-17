/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.panel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.theme.base.client.panel.FramedPanelBaseAppearance;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.Header.HeaderAppearance;

public class SlicedFramedPanelAppearance extends FramedPanelBaseAppearance {
  public interface FramedPanelStyle extends ContentPanelStyle {

  }

  public interface SlicedFramePanelResources extends ContentPanelResources {
    @Source("SlicedFramedPanel.gss")
    @Override
    FramedPanelStyle style();

    ThemeDetails theme();
  }

  public interface SlicedFramedPanelDivFrameResources extends SlicedFramedPanelFrame.Resources {
    ThemeDetails theme();

    @Source("SlicedFramedPanelDivFrame.gss")
    @Override
    SlicedFramePanelNestedDivFrameStyle style();

    @Source("framedpanel-background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("framedpanel-tl.png")
    @Override
    ImageResource topLeftBorder();

    @Source("framedpanel-tc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Source("framedpanel-tr.png")
    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

    @Source("framedpanel-l.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @Source("framedpanel-r.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @Source("framedpanel-bl.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomLeftBorder();

    @Source("framedpanel-bc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource bottomBorder();

    @Source("framedpanel-br.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomRightBorder();

  }
  public interface SlicedFramePanelNestedDivFrameStyle extends SlicedFramedPanelFrame.Style {

  }

  private final SlicedFramePanelResources resources;
  public SlicedFramedPanelAppearance() {
    this(GWT.<SlicedFramePanelResources>create(SlicedFramePanelResources.class));

  }

  public SlicedFramedPanelAppearance(SlicedFramePanelResources resources) {
    this(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class));
  }

  public SlicedFramedPanelAppearance(SlicedFramePanelResources resources, FramedPanelTemplate template) {
    this(resources, template, GWT.<SlicedFramedPanelDivFrameResources>create(SlicedFramedPanelDivFrameResources.class));
  }

  public SlicedFramedPanelAppearance(SlicedFramePanelResources res, SlicedFramedPanelDivFrameResources frameResources) {
    this(res, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), frameResources);
  }

  public SlicedFramedPanelAppearance(SlicedFramePanelResources resources, FramedPanelTemplate template, SlicedFramedPanelDivFrameResources frameResources) {
    super(resources, template, new SlicedFramedPanelFrame(frameResources));
    this.resources = resources;
  }

  @Override
  public HeaderAppearance getHeaderAppearance() {
    return new SlicedHeaderAppearance();
  }


  @Override
  public int getFrameHeight(XElement parent) {
    return resources.theme().framedPanel().border().top() + resources.theme().framedPanel().border().bottom();
  }

  @Override
  public int getFrameWidth(XElement parent) {
    return Math.max(resources.theme().framedPanel().borderRadius(), resources.theme().framedPanel().border().left())
        + Math.max(resources.theme().framedPanel().borderRadius(), resources.theme().framedPanel().border().right());
  }
}
