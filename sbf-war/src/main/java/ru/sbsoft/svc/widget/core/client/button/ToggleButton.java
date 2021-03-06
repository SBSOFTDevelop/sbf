/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.cell.core.client.form.ToggleButtonCell;

/**
 * A 2-state toggle button.
 */
public class ToggleButton extends CellButtonBase<Boolean> {

  /**
   * Creates a new toggle button.
   */
  public ToggleButton() {
    this(GWT.<ToggleButtonCell> create(ToggleButtonCell.class));
  }

  /**
   * Creates a new toggle button.
   * 
   * @param text the button text
   */
  public ToggleButton(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new toggle button.
   * 
   * @param cell the toggle button cell
   */
  public ToggleButton(ToggleButtonCell cell) {
    super(cell, false);
  }

  @Override
  public ToggleButtonCell getCell() {
    return (ToggleButtonCell) super.getCell();
  }

  /**
   * Returns the allow depress state.
   * 
   * @return the allow depress state
   */
  public boolean isAllowDepress() {
    return getCell().isAllowDepress();
  }

  /**
   * True to allow a toggle item to be depressed (defaults to true).
   * 
   * @param allowDepress true to allow depressing
   */
  public void setAllowDepress(boolean allowDepress) {
    getCell().setAllowDepress(allowDepress);
  }

  @Override
  protected void onFocus(Event ce) {
    if (!getValue()) {
      super.onFocus(ce);
    }
  }

}
