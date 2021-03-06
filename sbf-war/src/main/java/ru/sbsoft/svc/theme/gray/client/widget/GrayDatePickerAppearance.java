/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.theme.base.client.widget.DatePickerBaseAppearance;

public class GrayDatePickerAppearance extends DatePickerBaseAppearance {

  public interface GrayDatePickerResources extends DatePickerResources, ClientBundle {
    @Source({"ru/sbsoft/svc/theme/base/client/widget/DatePicker.gss", "GrayDatePicker.gss"})
    GrayDatePickerStyle css();
    
    @ImageOptions(preventInlining = true)
    ImageResource downIcon();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource footer();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource header();

    ImageResource leftButton();

    @ImageOptions(preventInlining = true, repeatStyle = RepeatStyle.None)
    ImageResource leftIcon();

    ImageResource rightButton();

    @ImageOptions(preventInlining = true, repeatStyle = RepeatStyle.None)
    ImageResource rightIcon();

  }

  public interface GrayDatePickerStyle extends DatePickerStyle {

  }

  public GrayDatePickerAppearance() {
    this(GWT.<GrayDatePickerResources> create(GrayDatePickerResources.class));
  }

  public GrayDatePickerAppearance(GrayDatePickerResources resources) {
    super(resources);
  }

}
