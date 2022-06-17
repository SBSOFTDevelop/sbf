/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Configuration settings for {@link Info} which supports a title and text.
 */
public class DefaultInfoConfig extends InfoConfig {

  public interface DefaultInfoConfigAppearance {

    void render(SafeHtmlBuilder sb, SafeHtml title, SafeHtml message);
  }

  private DefaultInfoConfigAppearance appearance;
  private SafeHtml title = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private SafeHtml message = SafeHtmlUtils.EMPTY_SAFE_HTML;

  /**
   * Creates a new config for an Info to display.
   * 
   * @param titleText the title text
   * @param messageText the message text
   */
  public DefaultInfoConfig(String titleText, String messageText) {
    this((DefaultInfoConfigAppearance) GWT.create(DefaultInfoConfigAppearance.class), titleText, messageText);
  }

  /**
   * Creates a new config for an Info to display.
   * 
   * @param titleText the title as HTML
   * @param messageText the message as HTML
   */
  public DefaultInfoConfig(SafeHtml titleText, SafeHtml messageText) {
    this((DefaultInfoConfigAppearance) GWT.create(DefaultInfoConfigAppearance.class), titleText, messageText);
  }

  /**
   * Creates a new config for an Info to display.
   * 
   * @param appearance the appearance to use
   * @param titleText the title text
   * @param messageText the message text
   */
  public DefaultInfoConfig(DefaultInfoConfigAppearance appearance, String titleText, String messageText) {
    this(appearance, SafeHtmlUtils.fromString(titleText), SafeHtmlUtils.fromString(messageText));
  }

  /**
   * Creates a new config for an Info to display.
   * 
   * @param appearance the appearance to use
   * @param titleHtml the title as HTML
   * @param messageHtml the message as HTML
   */
  public DefaultInfoConfig(DefaultInfoConfigAppearance appearance, SafeHtml titleHtml, SafeHtml messageHtml) {
    this.appearance = appearance;
    this.title = titleHtml;
    this.message = messageHtml;
  }

  public DefaultInfoConfigAppearance getAppearance() {
    return appearance;
  }

  public SafeHtml getMessage() {
    return message;
  }

  public SafeHtml getTitle() {
    return title;
  }

  public void setMessage(String message) {
    setMessage(SafeHtmlUtils.fromString(message));
  }

  public void setTitle(String title) {
    setTitle(SafeHtmlUtils.fromString(title));
  }

  public void setTitle(SafeHtml title) {
    this.title = title;
  }

  public void setMessage(SafeHtml message) {
    this.message = message;
  }

  @Override
  protected SafeHtml render(Info info) {
    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder, title, message);
    return builder.toSafeHtml();
  }

}
