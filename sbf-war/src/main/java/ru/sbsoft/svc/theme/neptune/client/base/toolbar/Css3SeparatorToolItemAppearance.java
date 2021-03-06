/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.toolbar.SeparatorToolItem.SeparatorToolItemAppearance;

/**
 *
 */
public class Css3SeparatorToolItemAppearance implements SeparatorToolItemAppearance {

  public interface Css3SeparatorToolItemResources extends ClientBundle {
    @Source("Css3SeparatorToolItem.gss")
    Css3SeparatorToolItemStyle style();

    ThemeDetails theme();
  }

  public interface Css3SeparatorToolItemStyle extends CssResource {
    String separator();
  }

  public interface Css3SeparatorToolItemTemplate extends XTemplates {
    @XTemplate("<div class=\"{style.separator}\"></div>")
    SafeHtml render(Css3SeparatorToolItemStyle style);
  }


  private final Css3SeparatorToolItemStyle style;
  private final Css3SeparatorToolItemTemplate template;

  public Css3SeparatorToolItemAppearance() {
    this(GWT.<Css3SeparatorToolItemResources>create(Css3SeparatorToolItemResources.class));
  }

  public Css3SeparatorToolItemAppearance(Css3SeparatorToolItemResources resources) {
    this(resources, GWT.<Css3SeparatorToolItemTemplate>create(Css3SeparatorToolItemTemplate.class));
  }

  public Css3SeparatorToolItemAppearance(Css3SeparatorToolItemResources resources, Css3SeparatorToolItemTemplate template) {
    this.style = resources.style();
    this.template = template;

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }
}
