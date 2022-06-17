/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import ru.sbsoft.svc.cell.core.client.form.TextAreaInputCell;
import ru.sbsoft.svc.cell.core.client.form.TextAreaInputCell.Resizable;
import ru.sbsoft.svc.core.client.dom.XElement;

/**
 * A multiple line text input field.
 */
public class TextArea extends ValueBaseField<String> {

  /**
   * Creates a new TextArea
   */
  public TextArea() {
    this(new TextAreaInputCell());
  }

  /**
   * Creates a new text area with the specified cell.
   * 
   * @param cell a text area input cell that renders the text area
   */
  public TextArea(TextAreaInputCell cell) {
    super(cell);
    redraw();
  }

  /**
   * Gets the cursor position.
   * 
   * @return returns the cursor position.
   */
  @Override
  public int getCursorPos() {
    return getImpl().getTextAreaCursorPos(getInputEl());
  }

  /**
   * Returns the resizable value.
   * 
   * @return the resize value
   */
  public Resizable getResizable() {
    return ((TextAreaInputCell) getCell()).getResizable();
  }

  /**
   * Gets the selection length
   * 
   * @return returns the selection length.
   */
  @Override
  public int getSelectionLength() {
    return getImpl().getTextAreaSelectionLength(getInputEl());
  }

  /**
   * Returns true if scroll bars are disabled.
   * 
   * @return the scroll bar state
   */
  public boolean isPreventScrollbars() {
    return ((TextAreaInputCell) getCell()).isPreventScrollbars();
  }
  
  @Override
  protected void onRedraw() {
    super.onRedraw();
    XElement input = getElement().selectNode("textarea");
    if (input != null) {
      input.setId(getId() + "-input");
    }
  }

  /**
   * True to prevent scrollbars from appearing regardless of how much text is in
   * the field (equivalent to setting overflow: hidden, defaults to false.
   * 
   * @param preventScrollbars true to disable scroll bars
   */
  public void setPreventScrollbars(boolean preventScrollbars) {
    ((TextAreaInputCell) getCell()).setPreventScrollbars(getElement(), preventScrollbars);
  }

  /**
   * Sets whether the field can be resized (defaults to NONE). This method uses
   * the CSS resize property which is only supported on browsers that support
   * CSS3.
   * 
   * @param resizable the resizable value
   */
  public void setResizable(Resizable resizable) {
    ((TextAreaInputCell) getCell()).setResizable(getElement(), resizable);
  }
}
