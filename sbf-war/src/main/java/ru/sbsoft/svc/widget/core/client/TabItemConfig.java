/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;
import ru.sbsoft.svc.core.shared.ExpandedHtmlSanitizer;
import ru.sbsoft.svc.core.client.util.HasUiAttributes;

/**
 * Config object which controls the content and behavior of a widget in a
 * TabPanel.
 * 
 * <p />
 * When updating the config object after the widget has been inserted, you must
 * call
 * {@link TabPanel#update(com.google.gwt.user.client.ui.Widget, TabItemConfig)}.
 */
public class TabItemConfig implements HasIcon, HasHTML, HasSafeHtml, HasUiAttributes {

  private boolean closable;
  private SafeHtml content = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private ImageResource icon;
  private boolean enabled = true;

  /**
   * Creates a tab item configuration.
   */
  public TabItemConfig() {

  }

  /**
   * Creates a tab item configuration with the specified text.
   * 
   * @param text the text of the tab item.
   */
  public TabItemConfig(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a tab item configuration with the specified properties.
   * 
   * @param text the tab item text
   * @param close true to indicate the tab is closable 
   */
  public TabItemConfig(String text, boolean close) {
    this();
    setText(text);
    setClosable(close);
  }

  /**
   * Returns the content of the label.
   *
   * @return the content of the label
   */
  public SafeHtml getContent() {
    return content;
  }

  /**
   * Sets the content of the label as html.
   *
   * @param html the label content html
   */
  public void setContent(SafeHtml html) {
    content = html;
  }

  /**
   * Sets the content of the label as text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the label content text
   */
  public void setContent(String text) {
    content = SafeHtmlUtils.fromString(text);
  }

  /**
   * Returns the content of the label as text.
   *
   * If text was set that contained reserved html characters, the return value will be html escaped.
   * If html was set instead, the return value will be html.
   *
   * @return the text or html, depending on what was set
   * @see #getHTML()
   */
  @Override
  public String getText() {
    return getHTML();
  }

  /**
   * Sets the content of the label as text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  @Override
  public void setText(String text) {
    setContent(text);
  }

  /**
   * Returns the content of the label as html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return content.asString();
  }

  /**
   * Sets the content of the label as html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    setContent(html);
  }

  /**
   * Sets the content of the label as html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    setContent(ExpandedHtmlSanitizer.sanitizeHtml(html));
  }

  @Override
  public ImageResource getIcon() {
    return icon;
  }

  /**
   * Returns the item closable state.
   * 
   * @return true if closable
   */
  public boolean isClosable() {
    return closable;
  }

  /**
   * Returns the enable / disable state.
   * 
   * @return true if enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * True to allow the item to be closable (defaults to false).
   * 
   * @param closable true for closable
   */
  public void setClosable(boolean closable) {
    this.closable = closable;
  }

  /**
   * True to enable, false to disable (defaults to true).
   * 
   * @param enabled the enabled state
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
  }

}
