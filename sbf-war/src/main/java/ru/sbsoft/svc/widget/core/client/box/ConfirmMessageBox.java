/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * A message box that prompts for confirmation with YES and NO buttons.
 * <p />
 * Code snippet:
 * 
 * <pre>
    final ConfirmMessageBox mb = new ConfirmMessageBox("Confirmation Required", "Are you ready?");
    mb.addDialogHideHandler(new DialogHideHandler() {
      {@literal @}Override
      public void onDialogHide(DialogHideEvent event) {
        switch (event.getHideButton()) {
          case YES:
            //Perform YES action
            break;
          case NO:
            //perform NO action
            break;
          default:
            //error, button added with no specific action ready
        }
      }
    });
    mb.setWidth(300);
    mb.show();
 * </pre>
 */
public class ConfirmMessageBox extends MessageBox {

  /**
   * Creates a message box that prompts for confirmation with YES and NO
   * buttons.
   *
   * @param titleText the title of the message box
   * @param messageText the message that appears in the message box
   */
  public ConfirmMessageBox(String titleText, String messageText) {
    this(SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a message box that prompts for confirmation with YES and NO
   * buttons.
   *
   * @param titleHtml the title of the message box
   * @param messageHtml the message that appears in the message box
   */
  public ConfirmMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box that prompts for confirmation with YES and NO
   * buttons.
   *
   * @param titleHtml the title of the message box
   * @param messageHtml the message that appears in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public ConfirmMessageBox(SafeHtml titleHtml, SafeHtml messageHtml, WindowAppearance windowAppearance,
                           MessageBoxAppearance messageBoxAppearance) {
    super(titleHtml, messageHtml, windowAppearance, messageBoxAppearance);

    setIcon(ICONS.question());
    setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
  }

}
