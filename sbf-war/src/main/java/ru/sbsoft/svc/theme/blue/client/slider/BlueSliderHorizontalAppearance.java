/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.blue.client.slider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.cell.core.client.SliderCell.HorizontalSliderAppearance;
import ru.sbsoft.svc.theme.base.client.slider.SliderHorizontalBaseAppearance;

public class BlueSliderHorizontalAppearance extends SliderHorizontalBaseAppearance implements HorizontalSliderAppearance {

  public interface BlueHorizontalSliderStyle extends SliderHorizontalStyle, CssResource {
  }

  public interface BlueSliderHorizontalResources extends SliderHorizontalResources, ClientBundle {

    @Source({
        "ru/sbsoft/svc/theme/base/client/slider/Slider.gss",
        "ru/sbsoft/svc/theme/base/client/slider/SliderHorizontal.gss", "BlueSliderHorizontal.gss"})
    BlueHorizontalSliderStyle style();

    ImageResource thumbHorizontal();

    ImageResource thumbHorizontalDown();

    ImageResource thumbHorizontalOver();

    ImageResource trackHorizontalLeft();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource trackHorizontalMiddle();

    ImageResource trackHorizontalRight();

  }

  public BlueSliderHorizontalAppearance() {
    this(GWT.<BlueSliderHorizontalResources> create(BlueSliderHorizontalResources.class),
        GWT.<SliderHorizontalTemplate> create(SliderHorizontalTemplate.class));
  }

  public BlueSliderHorizontalAppearance(BlueSliderHorizontalResources resources,
      SliderHorizontalTemplate template) {
    super(resources, template);
  }

}
