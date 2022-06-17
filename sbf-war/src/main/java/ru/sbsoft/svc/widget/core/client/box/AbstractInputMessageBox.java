/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.box;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.svc.core.client.Style.Side;
import ru.sbsoft.svc.widget.core.client.ComponentHelper;
import ru.sbsoft.svc.widget.core.client.form.Field;

/**
 * Abstract base class for message boxes containing an input field.
 */
public abstract class AbstractInputMessageBox extends MessageBox {

  /**
   * Input for the message box prompt
   */
  protected Field<String> field;

  /**
   * Creates a messageHtml box that prompts for input.
   *
   * @param titleHtml the titleHtml of the messageHtml box
   * @param messageHtml the messageHtml that appears in the messageHtml box
   * @param windowAppearance the messageHtml box window appearance
   * @param messageBoxAppearance the messageHtml box content appearance
   */
  protected AbstractInputMessageBox(Field<String> field, SafeHtml titleHtml, SafeHtml messageHtml,
                                    WindowAppearance windowAppearance, MessageBoxAppearance messageBoxAppearance) {
    super(titleHtml, messageHtml, windowAppearance, messageBoxAppearance);

    ComponentHelper.setParent(this, field);

    this.field = field;

    setFocusWidget(field);

    messageBoxAppearance.getContentElement(getElement()).appendChild(field.getElement());
    setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
  }

  /**
   * Returns the input field.
   * 
   * @return the input field
   */
  public Field<String> getField() {
    return field;
  }

  /**
   * Returns the current value of the input field.
   * 
   * @return the value of the input field
   */
  public String getValue() {
    return field.getValue();
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(field);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(field);
  }

  /**
   * Resize the field width to fit the content box.
   */
  @Override
  protected void resizeContents() {
    int width = getAppearance().getContentElem(getElement()).getWidth(true);
    int padding = getMessageBoxAppearance().getContentElement(getElement()).getPadding(Side.LEFT, Side.RIGHT);

    field.setWidth(width - padding);
  }

}
