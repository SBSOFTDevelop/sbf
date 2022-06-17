/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.ResourceGeneratorType;
import ru.sbsoft.svc.themebuilder.base.rebind.DetailsResourceGenerator;

@ResourceGeneratorType(DetailsResourceGenerator.class)
public interface ThemeDetails extends ResourcePrototype {

  AccordionLayoutDetails accordionLayout();

  @TypeDetails(sampleValue = "#000000", comment = "Default color to use on borders in the theme")
  String borderColor();

  @TypeDetails(sampleValue = "#000000", comment = "Default color to use for backgrounds, usually within panels and the like")
  String backgroundColor();

  @TypeDetails(sampleValue = "0.6", comment = "Opactiy value to use on disabled elements/widgets")
  double disabledOpacity();

  @TypeDetails(sampleValue = "'gray'", comment = "Text color to use in disabled widgets. Can be left blank to not set a color and instead let widgets retain their defaults")
  String disabledTextColor();

  BorderLayoutDetails borderLayout();

  ButtonDetails button();

  ButtonGroupDetails buttonGroup();

  FieldDetails field();

  DatePickerDetails datePicker();

  PanelDetails panel();

  FramedPanelDetails framedPanel();

  MenuDetails menu();

  SplitBarDetails splitbar();

  WindowDetails window();

  TabDetails tabs();

  ToolBarDetails toolbar();

  ToolIconDetails tools();

  InfoDetails info();

  FieldSetDetails fieldset();

  TipDetails tip();

  TreeDetails tree();

  TipDetails errortip();

  GridDetails grid();

  ListViewDetails listview();

  StatusDetails status();

  MaskDetails mask();

  ProgressBarDetails progressbar();

  StatusProxyDetails statusproxy();

  ColorPaletteDetails colorpalette();

  MessageBoxDetails messagebox();

  ResizableDetails resizable();
}
