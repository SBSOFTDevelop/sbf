/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHTML;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.shared.ExpandedHtmlSanitizer;
import ru.sbsoft.svc.widget.core.client.Component;

public class MenuBarItem extends Component implements HasSafeHtml, HasHTML {

  public static interface MenuBarItemAppearance {

    XElement getHtmlElement(XElement parent);

    void onOver(XElement parent, boolean over);

    void onActive(XElement parent, boolean active);

    void render(SafeHtmlBuilder builder);
  }

  private final MenuBarItemAppearance appearance;
  protected SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;
  protected Menu menu;
  protected boolean expanded;

  @UiConstructor
  public MenuBarItem(String text) {
    this(text, null);
  }

  public MenuBarItem(SafeHtml html) {
    this(html, null);
  }

  public MenuBarItem(String text, Menu menu) {
    this(text, menu, GWT.<MenuBarItemAppearance> create(MenuBarItemAppearance.class));
  }

  public MenuBarItem(SafeHtml html, Menu menu) {
    this(html, menu, GWT.<MenuBarItemAppearance> create(MenuBarItemAppearance.class));
  }

  public MenuBarItem(String text, Menu menu, MenuBarItemAppearance appearance) {
    this(SafeHtmlUtils.fromString(text), menu, appearance);
  }

  public MenuBarItem(SafeHtml html, Menu menu, MenuBarItemAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));
    sinkEvents(Event.MOUSEEVENTS);

    setHTML(html);
    setMenu(menu);
  }

  public MenuBarItemAppearance getAppearance() {
    return appearance;
  }

  public Menu getMenu() {
    return menu;
  }

  @UiChild(limit = 1, tagname = "menu")
  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    switch (event.getTypeInt()) {
      case Event.ONMOUSEOVER:
        appearance.onOver(getElement(), true);
        break;

      case Event.ONMOUSEOUT:
        appearance.onOver(getElement(), false);
        break;
    }
  }

  /**
   * Returns the item's text.
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
   * Sets the item's text.
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
   * Returns the item's html.
   *
   * @return the html
   */
  public SafeHtml getSafeHtml() {
    return html;
  }

  /**
   * Returns the item's html.
   *
   * @return the html
   */
  @Override
  public String getHTML() {
    return html.asString();
  }

  /**
   * Sets the item's html.
   *
   * @param html the html
   */
  @Override
  public void setHTML(SafeHtml html) {
    this.html = html;
    getAppearance().getHtmlElement(getElement()).setInnerSafeHtml(html);
  }

  /**
   * Sets the item's html.
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
