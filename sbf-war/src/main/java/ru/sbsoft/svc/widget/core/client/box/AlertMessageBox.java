/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * A message box that displays an error icon.
 */
public class AlertMessageBox extends MessageBox {

  /**
   * Creates a message box with an error icon and the specified title and
   * message.
   *
   * @param headingText the text to display for the message box heading
   * @param messageText the text to display in the message box
   */
  public AlertMessageBox(String headingText, String messageText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a message box with an error icon and the specified title and
   * message.
   *
   * @param titleHtml the message box title as html
   * @param messageHtml the message displayed in the message box as html
   */
  public AlertMessageBox(SafeHtml titleHtml, SafeHtml messageHtml) {
    this(titleHtml, messageHtml, GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a message box with an error icon and the specified title and
   * message.
   *
   * @param titleHtml the message box title as html
   * @param messageHtml the message displayed in the message box as html
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public AlertMessageBox(SafeHtml titleHtml, SafeHtml messageHtml, WindowAppearance windowAppearance,
                         MessageBoxAppearance messageBoxAppearance) {
    super(titleHtml, messageHtml, windowAppearance, messageBoxAppearance);

    setIcon(ICONS.error());
  }

}
