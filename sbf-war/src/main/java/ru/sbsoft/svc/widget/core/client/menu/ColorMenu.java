/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.menu;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import ru.sbsoft.svc.widget.core.client.ColorPalette;

public class ColorMenu extends Menu {

  protected ColorPalette palette;
  
  public ColorMenu() {
    palette = new ColorPalette();
    palette.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        onValueChanged(event);
      }
    });
    add(palette);
    plain = true;
    showSeparator = false;
    setEnableScrolling(false);
  }
  
  @Override
  public void focus() {
    super.focus();
    palette.getElement().focus();
  }
  
  public String getColor() {
    return palette.getValue();
  }
  
  public ColorPalette getPalette() {
    return palette;
  }
  
  public void setColor(String color) {
    palette.setValue(color);
  }
  
  protected void onValueChanged(ValueChangeEvent<String> event) {
    
  }
  
}
