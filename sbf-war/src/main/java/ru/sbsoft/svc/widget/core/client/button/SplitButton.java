/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.button;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.cell.core.client.ButtonCell.ButtonCellAppearance;
import ru.sbsoft.svc.cell.core.client.SplitButtonCell;
import ru.sbsoft.svc.widget.core.client.event.ArrowClickEvent;
import ru.sbsoft.svc.widget.core.client.event.ArrowClickEvent.ArrowClickHandler;
import ru.sbsoft.svc.widget.core.client.event.ArrowClickEvent.HasArrowClickHandlers;

/**
 * A split button that provides a built-in dropdown arrow that can fire an event
 * separately from the default click event of the button.
 */
public class SplitButton extends TextButton implements HasArrowClickHandlers {

  public interface SplitButtonAppearance extends ButtonCellAppearance<String> {

  }

  /**
   * Creates a new split button.
   */
  public SplitButton() {
    this(new SplitButtonCell());
  }

  /**
   * Creates a new split button.
   * 
   * @param cell the button's cell
   */
  public SplitButton(SplitButtonCell cell) {
    super(cell);
  }

  /**
   * Creates a new split button.
   * 
   * @param cell the button's cell
   * @param text the button's text
   */
  public SplitButton(SplitButtonCell cell, String text) {
    super(cell, text);
  }

  /**
   * Creates a new split button.
   * 
   * @param text the button's text
   */
  public SplitButton(String text) {
    this(new SplitButtonCell(), text);
  }

  @Override
  public HandlerRegistration addArrowClickHandler(ArrowClickHandler handler) {
    return addHandler(handler, ArrowClickEvent.getType());
  }

  @Override
  protected void onClick(Event e) {
    e.preventDefault();
    hideToolTip();
  }

}
