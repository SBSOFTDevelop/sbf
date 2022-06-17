/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * A label tool item.
 */
public class LabelToolItem extends Component {

  public interface LabelToolItemAppearance {
    void render(SafeHtmlBuilder sb);
  }

  private SafeHtml label = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private final LabelToolItemAppearance appearance;

  /**
   * Creates a new label.
   */
  public LabelToolItem() {
    this(GWT.<LabelToolItemAppearance> create(LabelToolItemAppearance.class));
  }

  public LabelToolItem(LabelToolItemAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder markupBuilder = new SafeHtmlBuilder();
    this.appearance.render(markupBuilder);

    setElement((Element) XDOM.create(markupBuilder.toSafeHtml()));
  }

  /**
   * Creates a new label as text.
   *
   * Text that contains reserved html characters will be escaped.
   * 
   * @param text the label text
   */
  public LabelToolItem(String text) {
    this();
    setLabel(text);
  }

  /**
   * Creates a new label as html.
   *
   * @param html the label html
   */
  public LabelToolItem(SafeHtml html)  {
    this();
    setLabel(html);
  }

  public LabelToolItemAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the item's label.
   * 
   * @return the label
   */
  public SafeHtml getLabel() {
    return label;
  }

  /**
   * Sets the item's label as text.
   *
   * Text that contains reserved html characters will be escaped.
   * 
   * @param text the label text
   */
  public void setLabel(String text) {
    setLabel(SafeHtmlUtils.fromString(text));
  }

  /**
   * Sets the item's label as html.
   *
   * @param html the label html
   */
  public void setLabel(SafeHtml html) {
    this.label = html;
    getElement().setInnerSafeHtml(html);
  }

}
