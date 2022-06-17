/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.progressbar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.cell.core.client.ProgressBarCell.ProgressBarAppearance;
import ru.sbsoft.svc.cell.core.client.ProgressBarCell.ProgressBarAppearanceOptions;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.Format;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3ProgressBarAppearance implements ProgressBarAppearance {
  public interface Css3ProgressBarResources extends ClientBundle {

    @Source("Css3ProgressBar.gss")
    Css3ProgressBarStyles styles();

    ThemeDetails theme();
  }

  public interface Css3ProgressBarStyles extends CssResource {
    String wrap();

    String bar();

    String text();

    String textBack();
  }

  public interface Css3ProgressBarTemplate extends XTemplates {
    @XTemplate(source = "Css3ProgressBar.html")
    SafeHtml render(SafeHtml html, Css3ProgressBarStyles style, SafeStyles wrapStyles, SafeStyles progressBarStyles, SafeStyles progressTextStyles, SafeStyles widthStyles);
  }

  private final Css3ProgressBarStyles styles;
  private final Css3ProgressBarTemplate template;

  public Css3ProgressBarAppearance() {
    this(GWT.<Css3ProgressBarResources>create(Css3ProgressBarResources.class));
  }

  public Css3ProgressBarAppearance(Css3ProgressBarResources resources) {
    this(resources, GWT.<Css3ProgressBarTemplate>create(Css3ProgressBarTemplate.class));
  }

  public Css3ProgressBarAppearance(Css3ProgressBarResources resources, Css3ProgressBarTemplate template) {
    styles = resources.styles();
    StyleInjectorHelper.ensureInjected(styles, false);
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder sb, Double value, ProgressBarAppearanceOptions options) {
    value = value == null ? 0 : value;

    String text = options.getProgressText();

    if (text != null) {
      int v = (int) Math.round(value * 100);
      text = Format.substitute(text, v);
    }

    SafeHtml txt = Util.isEmptyString(text) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(text);

    SafeStyles widthStyles = SafeStylesUtils.fromTrustedNameAndValue("width", options.getWidth() + "px");

    final SafeStyles progressBarStyles;
    if (value <= 0) {
      progressBarStyles = SafeStylesUtils.fromTrustedNameAndValue("visibility", "hidden");
    } else {
      progressBarStyles = SafeStylesUtils.fromTrustedNameAndValue("width", value * 100 + "%");
    }


    sb.append(template.render(txt, styles, null, progressBarStyles, null, widthStyles));

  }
}
