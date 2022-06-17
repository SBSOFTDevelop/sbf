/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.toolbar.LabelToolItem.LabelToolItemAppearance;

public class LabelToolItemDefaultAppearance implements LabelToolItemAppearance {

  public interface LabelToolItemResources extends ClientBundle {

    @Source("LabelToolItem.gss")
    LabelToolItemStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource background();
  }

  public interface LabelToolItemStyle extends CssResource {

    String item();
  }

  public interface Template extends XTemplates {
    @XTemplate("<div class=\"{style.item}\"></div>")
    SafeHtml render(LabelToolItemStyle style);
  }

  protected LabelToolItemStyle style;
  protected Template template;

  public LabelToolItemDefaultAppearance() {
    this(GWT.<LabelToolItemResources> create(LabelToolItemResources.class));
  }

  public LabelToolItemDefaultAppearance(LabelToolItemResources resources) {
    this.style = resources.css();
    
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = GWT.create(Template.class);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

}
