/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.Style.Side;
import ru.sbsoft.svc.widget.core.client.ComponentHelper;
import ru.sbsoft.svc.widget.core.client.ProgressBar;

/**
 * A <code>MessageBox</code> which displays a {@link ProgressBar}.
 */
public class ProgressMessageBox extends MessageBox {

  private ProgressBar progressBar;
  private String progressText = "";
  private int minProgressWidth = 250;

  /**
   * Creates a progress message box with the specified heading HTML.
   *
   * @param headingText the text to display for the message box heading.
   */
  public ProgressMessageBox(String headingText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.EMPTY_SAFE_HTML);
  }

  /**
   * Creates a progress message box with the specified heading HTML.
   *
   * @param headingHtml the HTML to display for the message box heading
   */
  public ProgressMessageBox(SafeHtml headingHtml) {
    this(headingHtml, SafeHtmlUtils.EMPTY_SAFE_HTML);
  }

  /**
   * Creates a progress message box with the specified heading and message HTML.
   *
   * @param headingText the text to display for the message box heading
   * @param messageText the text to display in the message box
   */
  public ProgressMessageBox(String headingText, String messageText) {
    this(SafeHtmlUtils.fromString(headingText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a progress message box with the specified heading and message HTML.
   *
   * @param headingHtml the HTML to display for the message box heading
   * @param messageHtml the HTML to display in the message box
   */
  public ProgressMessageBox(SafeHtml headingHtml, SafeHtml messageHtml) {
    this(headingHtml, messageHtml,
        GWT.<WindowAppearance>create(WindowAppearance.class),
        GWT.<MessageBoxAppearance>create(MessageBoxAppearance.class));
  }

  /**
   * Creates a progress message box with the specified heading and message HTML.
   *
   * @param headingHtml the HTML to display for the message box heading
   * @param messageHtml the HTML to display in the message box
   * @param windowAppearance the message box window windowAppearance
   * @param messageBoxAppearance the message box content windowAppearance
   */
  public ProgressMessageBox(SafeHtml headingHtml, SafeHtml messageHtml, WindowAppearance windowAppearance,
                            MessageBoxAppearance messageBoxAppearance) {
    super(headingHtml, messageHtml, windowAppearance, messageBoxAppearance);

    progressBar = new ProgressBar();

    messageBoxAppearance.getContentElement(getElement()).appendChild(progressBar.getElement());

    progressBar.clearSizeCache();
    progressBar.setWidth(300 - getFrameSize().getWidth());

    setFocusWidget(progressBar);

    icon = null;
  }

  /**
   * Returns the minimum progress width.
   * 
   * @return the width
   */
  public int getMinProgressWidth() {
    return minProgressWidth;
  }

  /**
   * Returns the box's progress bar.
   * 
   * @return the progress bar
   */
  public ProgressBar getProgressBar() {
    return progressBar;
  }

  /**
   * Returns the progress text.
   * 
   * @return the progress text
   */
  public String getProgressText() {
    return progressText;
  }

  /**
   * The minimum width in pixels of the message box if it is a progress-style
   * dialog. This is useful for setting a different minimum width than text-only
   * dialogs may need (defaults to 250).
   * 
   * @param minProgressWidth the min progress width
   */
  public void setMinProgressWidth(int minProgressWidth) {
    this.minProgressWidth = minProgressWidth;
  }

  /**
   * The text to display inside the progress bar.
   * 
   * @param progressText the progress text
   */
  public void setProgressText(String progressText) {
    this.progressText = progressText;
  }

  /**
   * Updates a progress-style message box's text and progress bar.
   * 
   * @param value any number between 0 and 1 (e.g., .5)
   * @param text the progress text to display inside the progress bar or null
   */
  public void updateProgress(double value, String text) {
    if (progressBar != null) {
      progressBar.updateProgress(value, text);
    }
    return;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(progressBar);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(progressBar);
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (getProgressText() != null) {
      progressBar.updateText(getProgressText());
    }
  }

  /**
   * Resize the progress bar width to fit the content box.
   */
  @Override
  protected void resizeContents() {
    int width = getAppearance().getContentElem(getElement()).getWidth(true);
    int padding = getMessageBoxAppearance().getContentElement(getElement()).getPadding(Side.LEFT, Side.RIGHT);

    getProgressBar().setWidth(width - padding);
  }

}
