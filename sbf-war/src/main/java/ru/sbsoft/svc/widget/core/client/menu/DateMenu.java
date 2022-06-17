/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.menu;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.DatePicker;

public class DateMenu extends Menu implements HasValueChangeHandlers<Date>{

  protected DatePicker picker;

  public DateMenu() {
    this(new DatePicker());
  }

  public DateMenu(DatePicker datePicker) {
    this(datePicker, GWT.<MenuAppearance>create(MenuAppearance.class));
  }

  public DateMenu(DatePicker datePicker, MenuAppearance menuAppearance) {
    super(menuAppearance);

    this.picker = datePicker;

    picker.addValueChangeHandler(new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        onPickerSelect(event);
      }
    });

    add(picker);
    getAppearance().applyDateMenu(getElement());
    plain = true;
    showSeparator = false;
    setEnableScrolling(false);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  @Override
  public void focus() {
    super.focus();
    picker.getElement().focus();
  }

  /**
   * Returns the selected date.
   * 
   * @return the date
   */
  public Date getDate() {
    return picker.getValue();
  }

  /**
   * Returns the date picker.
   * 
   * @return the date picker
   */
  public DatePicker getDatePicker() {
    return picker;
  }

  /**
   * Sets the menu's date.
   * 
   * @param date the date
   */
  public void setDate(Date date) {
    picker.setValue(date);
  }

  protected void onPickerSelect(ValueChangeEvent<Date> event) {
    fireEvent(event);
  }

}
