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
import ru.sbsoft.svc.cell.core.client.SliderCell.VerticalSliderAppearance;
import ru.sbsoft.svc.theme.base.client.slider.SliderVerticalBaseAppearance;

public class BlueSliderVerticalAppearance extends SliderVerticalBaseAppearance implements VerticalSliderAppearance {

  public interface BlueSliderVerticalResources extends SliderVerticalResources, ClientBundle {

    @Override
    @Source({
        "ru/sbsoft/svc/theme/base/client/slider/Slider.gss",
        "ru/sbsoft/svc/theme/base/client/slider/SliderVertical.gss",
        "BlueSliderVertical.gss"})
    BlueVerticalSliderStyle style();

    ImageResource thumbVertical();

    ImageResource thumbVerticalDown();

    ImageResource thumbVerticalOver();

    ImageResource trackVerticalBottom();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource trackVerticalMiddle();

    ImageResource trackVerticalTop();

  }

  public interface BlueVerticalSliderStyle extends BaseSliderVerticalStyle, CssResource {
  }

  public BlueSliderVerticalAppearance() {
    this(GWT.<BlueSliderVerticalResources> create(BlueSliderVerticalResources.class),
        GWT.<SliderVerticalTemplate> create(SliderVerticalTemplate.class));
  }

  public BlueSliderVerticalAppearance(BlueSliderVerticalResources resources, SliderVerticalTemplate template) {
    super(resources, template);
  }

}
