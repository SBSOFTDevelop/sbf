/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.progress;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.cell.core.client.ProgressBarCell.ProgressBarAppearance;
import ru.sbsoft.svc.cell.core.client.ProgressBarCell.ProgressBarAppearanceOptions;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.util.Format;
import ru.sbsoft.svc.core.client.util.Util;

public class ProgressBarDefaultAppearance implements ProgressBarAppearance {

  public interface ProgressBarResources {

    ImageResource bar();

    ProgressBarStyle style();
  }
  public interface ProgressBarStyle extends CssResource {

    String progressBar();

    String progressInner();

    String progressText();

    String progressTextBack();

    String progressWrap();

  }

  public interface ProgressBarTemplate extends XTemplates {

    @XTemplate(source = "ProgressBar.html")
    SafeHtml render(SafeHtml html, ProgressBarStyle style, SafeStyles wrapStyles, SafeStyles progressBarStyles, SafeStyles progressTextStyles,
        SafeStyles widthStyles);

  }

  private final ProgressBarStyle style;
  private ProgressBarTemplate template;

  public ProgressBarDefaultAppearance(ProgressBarResources resources, ProgressBarTemplate template) {
    this.style = resources.style();
    this.style.ensureInjected();
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder sb, Double value, ProgressBarAppearanceOptions options) {
    value = value == null ? 0 : value;
    double valueWidth = value * options.getWidth();

    int vw = new Double(valueWidth).intValue();

    String text = options.getProgressText();

    if (text != null) {
      int v = (int) Math.round(value * 100);
      text = Format.substitute(text, v);
    }

    SafeHtml txt = Util.isEmptyString(text) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(text);

    int adj = SVC.isIE() ? 4 : 2;

    SafeStyles wrapStyles = SafeStylesUtils.fromTrustedString("width:" + (options.getWidth() - adj) + "px;");
    SafeStyles progressBarStyles = SafeStylesUtils.fromTrustedString("width:" + vw + "px;");
    SafeStyles progressTextStyles = SafeStylesUtils.fromTrustedString("width:" + Math.max(vw - 8, 0) + "px;");
    SafeStyles widthStyles = SafeStylesUtils.fromTrustedString("width:" + (Math.max(0, options.getWidth() - adj)) + "px;");
    sb.append(template.render(txt, style, wrapStyles, progressBarStyles, progressTextStyles, widthStyles));
  }

}