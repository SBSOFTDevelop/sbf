/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

/**
 *
 */
public interface FieldDetails {

  @TypeDetails(sampleValue = "1", comment = "border width of the field")
  int borderWidth();

  @TypeDetails(sampleValue = "#000000", comment = "border color of the field")
  String borderColor();

  @TypeDetails(sampleValue = "'solid'", comment = "border style of the field")
  String borderStyle();

  @TypeDetails(sampleValue = "#ffffff", comment = "background color of the inside of the field")
  String backgroundColor();

  @TypeDetails(sampleValue = "#cccccc", comment = "border color of the field when focused")
  String focusBorderColor();

  @TypeDetails(sampleValue = "#cccccc", comment = "background color of the field when invalid")
  String invalidBackgroundColor();

  @TypeDetails(sampleValue = "#ff0000", comment = "border color of the field when invalid")
  String invalidBorderColor();

  @TypeDetails(sampleValue = "1", comment = "border width of the field when invalid")
  int invalidBorderWidth();

  @TypeDetails(sampleValue = "18", comment = "height of textfields (other than text area")
  int height();

  @TypeDetails(sampleValue = "18px", comment = "line-height of textfields")
  String lineHeight();

  @TypeDetails(sampleValue = "util.padding(8, 12)", comment = "padding around fields")
  EdgeDetails padding();


  @TypeDetails(sampleValue = "#808080", comment = "color of the empty placeholder text")
  String emptyTextColor();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'medium')", comment = "text styling for fields")
  FontDetails text();

  SliderDetails slider();

  FieldLabelDetails sideLabel();

  FieldLabelDetails topLabel();

  CheckBoxDetails checkBox();

  RadioDetails radio();

  public interface CheckBoxDetails {
    @TypeDetails(sampleValue = "util.fontStyle('tahoma, arial, helvetica, sans-serif', '12px')", comment = "text styling check box labels")
    FontDetails boxLabel();

    @TypeDetails(sampleValue = "util.padding(0)", comment = "padding around the entire label, image sits in left padding")
    EdgeDetails padding();
  }

  public interface RadioDetails extends CheckBoxDetails {
    @TypeDetails(sampleValue = "util.fontStyle('tahoma, arial, helvetica, sans-serif', '12px')", comment = "text styling radio labels")
    FontDetails boxLabel();
  }

  public interface FieldLabelDetails {
    @TypeDetails(sampleValue = "util.padding(0, 0, 5)", comment = "spacing around the entire field label")
    EdgeDetails padding();

    @TypeDetails(sampleValue = "util.padding(3,0,0)", comment = "spacing around the label text")
    EdgeDetails labelPadding();

    @TypeDetails(sampleValue = "util.padding(0)", comment = "spacing around the field within the label")
    EdgeDetails fieldPadding();

    @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'medium')", comment = "text styling for field labels")
    FontDetails text();

    @TypeDetails(sampleValue = "'left'", comment = "alignment for the label text, may be 'center', 'left', or 'right'")
    String textAlign();
  }
}
