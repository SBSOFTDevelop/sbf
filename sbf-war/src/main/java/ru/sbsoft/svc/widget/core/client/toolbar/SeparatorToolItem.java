/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * A tool bar separator.
 */
public class SeparatorToolItem extends Component {

  public interface SeparatorToolItemAppearance {
    void render(SafeHtmlBuilder sb);
  }

  @SuppressWarnings("unused")
  private final SeparatorToolItemAppearance appearance;

  public SeparatorToolItem() {
    this(GWT.<SeparatorToolItemAppearance>create(SeparatorToolItemAppearance.class));
  }

  public SeparatorToolItem(SeparatorToolItemAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder markupBuilder = new SafeHtmlBuilder();
    appearance.render(markupBuilder);

    setElement((Element) XDOM.create(markupBuilder.toSafeHtml()));
  }

}
