/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.theme.base.client.frame.NestedDivFrame;
import ru.sbsoft.svc.theme.base.client.tips.TipDefaultAppearance;
import ru.sbsoft.svc.widget.core.client.form.error.SideErrorHandler.SideErrorTooltipAppearance;

public class ErrorTipDefaultAppearance extends TipDefaultAppearance implements SideErrorTooltipAppearance {

  public interface ErrorTipFrameResources extends ClientBundle, TipDivFrameResources {
    @Source("ru/sbsoft/svc/core/public/clear.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipBottomBorder.gif")
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipBottomLeftBorder.gif")
    ImageResource bottomLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipBottomRightBorder.gif")
    ImageResource bottomRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Source("errorTipLeftBorder.gif")
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipRightBorder.gif")
    @Override
    ImageResource rightBorder();

    @Source({"ru/sbsoft/svc/theme/base/client/frame/NestedDivFrame.gss", "ErrorTipFrame.gss"})
    @Override
    ErrorTipNestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("errorTipTopBorder.gif")
    @Override
    ImageResource topBorder();

    @Source("errorTipTopLeftBorder.gif")
    @Override
    ImageResource topLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipTopRightBorder.gif")
    ImageResource topRightBorder();
  }

  public interface ErrorTipNestedDivFrameStyle extends TipNestedDivFrameStyle {
  }

  public interface ErrorTipResources extends TipResources {
    @Source("exclamation.gif")
    @ImageOptions(preventInlining = true)
    ImageResource errorIcon();

    @Source({"ru/sbsoft/svc/theme/base/client/tips/TipDefault.gss", "ErrorTip.gss"})
    ErrorTipStyle style();
  }

  public interface ErrorTipStyle extends TipStyle {
    String textWrap();
  }

  public interface ErrorTipTemplate extends XTemplates {
    @XTemplate(source = "ErrorTipDefault.html")
    SafeHtml render(ErrorTipStyle style);
  }

  private ErrorTipTemplate template;

  public ErrorTipDefaultAppearance() {
    this(GWT.<ErrorTipResources> create(ErrorTipResources.class));
  }

  public ErrorTipDefaultAppearance(ErrorTipResources resources) {
    super(resources);

    template = GWT.create(ErrorTipTemplate.class);
    frame = new NestedDivFrame(GWT.<TipDivFrameResources> create(ErrorTipFrameResources.class));
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render((ErrorTipStyle) style));
  }

}
