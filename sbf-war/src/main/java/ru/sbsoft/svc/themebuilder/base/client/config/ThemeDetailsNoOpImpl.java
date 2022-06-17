/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;


public class ThemeDetailsNoOpImpl implements ThemeDetails {
  private static final RuntimeException EXCEPTION = new IllegalStateException("NoOp implementation called");


  @Override
  public AccordionLayoutDetails accordionLayout() {
    throw EXCEPTION;
  }

  @Override
  public String borderColor() {
    throw EXCEPTION;

  }

  @Override
  public String backgroundColor() {
    throw EXCEPTION;

  }

  @Override
  public double disabledOpacity() {
    throw EXCEPTION;

  }

  @Override
  public String disabledTextColor() {
    throw EXCEPTION;

  }

  @Override
  public BorderLayoutDetails borderLayout() {
    throw EXCEPTION;

  }

  @Override
  public ButtonDetails button() {
    throw EXCEPTION;

  }

  @Override
  public ButtonGroupDetails buttonGroup() {
    throw EXCEPTION;
  }


  @Override
  public FieldDetails field() {
    throw EXCEPTION;

  }

  @Override
  public DatePickerDetails datePicker() {
    throw EXCEPTION;

  }

  @Override
  public PanelDetails panel() {
    throw EXCEPTION;

  }

  @Override
  public FramedPanelDetails framedPanel() {
    throw EXCEPTION;

  }

  @Override
  public MenuDetails menu() {
    throw EXCEPTION;

  }

  @Override
  public SplitBarDetails splitbar() {
    throw EXCEPTION;

  }

  @Override
  public WindowDetails window() {
    throw EXCEPTION;

  }

  @Override
  public TabDetails tabs() {
    throw EXCEPTION;

  }

  @Override
  public ToolBarDetails toolbar() {
    throw EXCEPTION;

  }

  @Override
  public ToolIconDetails tools() {

    throw EXCEPTION;
  }

  @Override
  public InfoDetails info() {
    throw EXCEPTION;

  }

  @Override
  public FieldSetDetails fieldset() {
    throw EXCEPTION;

  }

  @Override
  public TipDetails tip() {
    throw EXCEPTION;

  }

  @Override
  public TreeDetails tree() {
    throw EXCEPTION;

  }

  @Override
  public TipDetails errortip() {
    throw EXCEPTION;

  }

  @Override
  public GridDetails grid() {
    throw EXCEPTION;

  }

  @Override
  public ListViewDetails listview() {
    throw EXCEPTION;

  }

  @Override
  public StatusDetails status() {
    throw EXCEPTION;

  }

  @Override
  public MaskDetails mask() {
    throw EXCEPTION;

  }

  @Override
  public ProgressBarDetails progressbar() {
    throw EXCEPTION;

  }

  @Override
  public StatusProxyDetails statusproxy() {
    throw EXCEPTION;

  }

  @Override
  public ColorPaletteDetails colorpalette() {
    throw EXCEPTION;

  }

  @Override
  public MessageBoxDetails messagebox() {
    throw EXCEPTION;

  }

  @Override
  public ResizableDetails resizable() {
    throw EXCEPTION;
  }

  @Override
  public String getName() {
    throw EXCEPTION;
  }
}
