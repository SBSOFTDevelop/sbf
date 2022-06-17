/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.shared.ExpandedHtmlSanitizer;

/**
 * A widget that displays a status message and icon, typically used in a tool
 * bar.
 */
public class Status extends Component implements HasText, HasHTML, HasIcon, HasSafeHtml {

  @SuppressWarnings("javadoc")
  public interface StatusAppearance {

    ImageResource getBusyIcon();

    XElement getHtmlElement(XElement parent);

    void onUpdateIcon(XElement parent, ImageResource icon);

    void render(SafeHtmlBuilder sb);

  }
  
  public interface BoxStatusAppearance extends StatusAppearance {
    
  }

  private final StatusAppearance appearance;

  private SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private ImageResource icon;

  /**
   * Creates a status component with the default appearance.
   */
  public Status() {
    this(GWT.<StatusAppearance> create(StatusAppearance.class));
  }

  /**
   * Creates a status component with the specified appearance.
   * 
   * @param appearance the appearance of the status widget.
   */
  public Status(StatusAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));
  }

  /**
   * Clears the current status by removing the current icon and change the text.
   * 
   * @param text the new text value
   */
  public void clearStatus(String text) {
    setIcon(null);
    setText(text);
  }

  /**
   * Clears the current status by removing the current icon and change the html.
   *
   * @param html the new html value
   */
  public void clearStatus(SafeHtml html) {
    setIcon(null);
    setHTML(html);
  }

  public StatusAppearance getAppearance() {
    return appearance;
  }

  /**
   * Enables a busy icon and displays the given text.
   * 
   * @param text the text to display
   */
  public void setBusy(String text) {
    setIcon(appearance.getBusyIcon());
    setText(text);
  }

  /**
   * Enables a busy icon and displays the given html.
   *
   * @param html the html to display
   */
  public void setBusy(SafeHtml html) {
    setIcon(appearance.getBusyIcon());
    setHTML(html);
  }

  @Override
  public ImageResource getIcon() {
    return icon;
  }

  @Override
  public void setIcon(ImageResource icon) {
    this.icon = icon;
    appearance.onUpdateIcon(getElement(), icon);
  }

  /**
   * Returns the status text.
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
   * Sets the status text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  @Override
  public void setText(String text) {
    setHTML(SafeHtmlUtils.fromString(text));
  }

  /**
   * Returns the status html.
   *
   * @return the html
   */
  public SafeHtml getSafeHtml() {
    return html;
  }

  /**
   * Returns the status html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return html.asString();
  }

  /**
   * Sets the status html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    this.html = html;
    getAppearance().getHtmlElement(getElement()).setInnerSafeHtml(html);
  }

  /**
   * Sets the status html.
   *
   * Untrusted html will be sanitized before use to protect against XSS.
   *
   * @param html the html
   */
  @Override
  public void setHTML(String html) {
    setHTML(ExpandedHtmlSanitizer.sanitizeHtml(html));
  }

}
