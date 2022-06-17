/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.TextArea;

/**
 * A message box that prompts for input with a multiple line text area and OK
 * and CANCEL buttons.
 * <p />
 * Code snippet:
 * 
 * <pre>
     MultiLinePromptMessageBox mb = new MultiLinePromptMessageBox("Description", "Please enter a brief description");
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
public class MultiLinePromptMessageBox extends AbstractInputMessageBox {

  /**
   * Creates a messageText box that prompts for input with a multiple line text area
   * and OK and CANCEL buttons.
   *
   * @param titleText the titleText of the messageText box
   * @param messageText the messageText that appears in the messageText box
   */
  public MultiLinePromptMessageBox(String titleText, String messageText) {
    this(SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a message box that prompts for input with a multiple line text area
   * and OK and CANCEL buttons.
   *
   * @param titleHtml the title of the message box
   * @param messageHtml the message that appears in the message box
   */
  public MultiLinePromptMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box that prompts for input with a multiple line text area
   * and OK and CANCEL buttons.
   *
   * @param title the title of the message box
   * @param message the message that appears in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public MultiLinePromptMessageBox(SafeHtml title, SafeHtml message,
                                   WindowAppearance windowAppearance, MessageBoxAppearance messageBoxAppearance) {
    super(new TextArea(), title, message, windowAppearance, messageBoxAppearance);

    getField().setHeight(75);
  }

  /**
   * Returns the multiple line text area.
   * 
   * @return the multiple line text area
   */
  public TextArea getTextArea() {
    return (TextArea) field;
  }

}
