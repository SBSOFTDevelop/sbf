/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.TextField;

/**
 * A message box that prompts for input with a single line text field and OK and
 * CANCEL buttons.
 * <p />
 * Code snippet:
 *
 * <pre>
    final PromptMessageBox mb = new PromptMessageBox("Description", "Please enter a brief description");
    mb.addDialogHideHandler(new DialogHideHandler() {
      {@literal @}Override
      public void onDialogHide(DialogHideEvent event) {
        if (event.getHideButton() == PredefinedButton.OK) {
          // perform OK action
        } else if (event.getHideButton() == PredefinedButton.CANCEL) {
          // perform CANCEL action
        }
      }
    });
    mb.setWidth(300);
    mb.show();
 * </pre>
 */
public class PromptMessageBox extends AbstractInputMessageBox {

  /**
   * Creates a messageText box that prompts for input with a single line text field
   * and OK and CANCEL buttons.
   *
   * @param titleText the titleText of the messageText box
   * @param messageText the messageText that appears in the messageText box
   */
  public PromptMessageBox(String titleText, String messageText) {
    this(SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a messageHtml box that prompts for input with a single line text field
   * and OK and CANCEL buttons.
   *
   * @param titleHtml the titleHtml of the messageHtml box
   * @param messageHtml the messageHtml that appears in the messageHtml box
   */
  public PromptMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, (WindowAppearance) GWT.create(WindowAppearance.class),
        (MessageBoxAppearance) GWT.create(MessageBoxAppearance.class));
  }

  /**
   * Creates a messageHtml box that prompts for input with a single line text field
   * and OK and CANCEL buttons.
   *
   * @param titleHtml the titleHtml of the messageHtml box
   * @param messageHtml the messageHtml that appears in the messageHtml box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public PromptMessageBox(SafeHtml titleHtml, SafeHtml messageHtml,
                          WindowAppearance windowAppearance, MessageBoxAppearance messageBoxAppearance) {
    super(new TextField(), titleHtml, messageHtml, windowAppearance, messageBoxAppearance);
  }

  /**
   * Returns the single line text field.
   *
   * @return the single line text field
   */
  public TextField getTextField() {
    return (TextField) field;
  }

}
