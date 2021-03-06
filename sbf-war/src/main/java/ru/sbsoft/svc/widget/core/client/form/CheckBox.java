/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.form;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.cell.core.client.form.CheckBoxCell;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.DelayedTask;

/**
 * Simple checkbox field. {@link ValueChangeEvent}s are fired when the checkbox
 * state is changed by the user, instead of waiting for a {@link BlurEvent}.
 */
public class CheckBox extends Field<Boolean> implements HasChangeHandlers {

  private DelayedTask alignTask = new DelayedTask() {
    @Override
    public void onExecute() {
      alignElements();
    }
  };

  /**
   * Creates a new check box.
   */
  public CheckBox() {
    this(new CheckBoxCell());
  }

  /**
   * Creates a new check box.
   * 
   * @param cell the check box cell
   */
  public CheckBox(CheckBoxCell cell) {
    super(cell);

    setValue(false, false);
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  /**
   * Clears the value from the field.
   */
  public void clear() {
    boolean restore = preventMark;
    preventMark = true;
    setValue(false, false);
    preventMark = restore;
    clearInvalid();
  }

  @Override
  public void clearInvalid() {
    forceInvalidText = null;
    // do nothing else, there are no validation errors possible on a checkbox
  }

  /**
   * Returns the box label.
   * 
   * @return the box label
   */
  public SafeHtml getBoxLabel() {
    return getCell().getBoxLabel();
  }

  @Override
  public CheckBoxCell getCell() {
    return (CheckBoxCell) super.getCell();
  }

  /**
   * The text that appears beside the checkbox (defaults to empty string).
   *
   * @param text the box label text
   */
  public void setBoxLabel(String text) {
    getCell().setBoxLabel(getElement(), SafeHtmlUtils.fromString(text));
  }

  /**
   * The html that appears beside the checkbox (defaults to empty string).
   * 
   * @param html the box label html
   */
  public void setBoxLabel(SafeHtml html) {
    getCell().setBoxLabel(getElement(), html);
  }

  @Override
  public void setTabIndex(int tabIndex) {
    this.tabIndex = tabIndex;
    getCell().getInputElement(getElement()).setTabIndex(tabIndex);
  }

  @Override
  public void setValue(Boolean value) {
    // TODO if we support a default value, replace this with that
    if (value == null) {
      value = false;
    }
    setValue(value, false);
    redraw();
  }

  protected void alignElements() {
    if (getBoxLabel() == null) {
      getCell().getInputElement(getElement()).<XElement> cast().center(getElement());
    }
  }

  @Override
  protected void markInvalid(List<EditorError> msg) {
    // do nothing
  }

  @Override
  public void reset() {
    if (originalValue == null) {
      originalValue = false;
    }
    super.reset();
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    alignTask.delay(10);
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    alignTask.delay(10);
  }

  @Override
  protected void onRedraw() {
    super.onRedraw();
    getCell().getInputElement(getElement()).setTabIndex(getTabIndex());
    alignTask.delay(10);
  }

}
