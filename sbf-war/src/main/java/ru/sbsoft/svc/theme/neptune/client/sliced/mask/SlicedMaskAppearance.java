/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.sliced.mask;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.Mask.MaskAppearance;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.theme.base.client.frame.DivFrame;
import ru.sbsoft.svc.theme.base.client.frame.DivFrame.DivFrameResources;
import ru.sbsoft.svc.theme.base.client.frame.DivFrame.DivFrameStyle;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class SlicedMaskAppearance implements MaskAppearance {
  public interface SlicedMaskTemplate extends XTemplates {
    @XTemplate("<div class=\"{style.text}\">{message}</div>")
    SafeHtml template(SlicedMaskStyles style, String message);
  }

  public interface SlicedMaskResources extends DivFrameResources, ClientBundle {
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/frame/DivFrame.gss", "SlicedMask.gss"})
    SlicedMaskStyles style();

    @Source("ru/sbsoft/svc/theme/base/client/grid/loading.gif")
    ImageResource loading();

    @Source("background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Override
    @Source("corner-bc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bottomBorder();

    @Override
    @Source("corner-bl.png")
    ImageResource bottomLeftBorder();

    @Override
    @Source("corner-br.png")
    ImageResource bottomRightBorder();

    @Override
    @Source("side-l.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource leftBorder();

    @Override
    @Source("side-r.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource rightBorder();

    @Override
    @Source("corner-tc.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource topBorder();

    @Override
    @Source("corner-tl.png")
    ImageResource topLeftBorder();

    @Override
    @Source("corner-tr.png")
    ImageResource topRightBorder();

    ThemeDetails theme();
  }

  public interface SlicedMaskStyles extends DivFrameStyle {
    String mask();

    String text();

    String masked();

    String positioned();
  }

  private final SlicedMaskResources resources;
  private final DivFrame frame;
  private final SlicedMaskTemplate template;

  public SlicedMaskAppearance() {
    this(GWT.<SlicedMaskResources>create(SlicedMaskResources.class));
  }

  public SlicedMaskAppearance(SlicedMaskResources resources) {
    this(resources, GWT.<SlicedMaskTemplate>create(SlicedMaskTemplate.class));
  }

  public SlicedMaskAppearance(SlicedMaskResources resources, SlicedMaskTemplate template) {
    this(resources, template, new DivFrame(resources));
  }

  public SlicedMaskAppearance(SlicedMaskResources resources, SlicedMaskTemplate template, DivFrame frame) {
    this.resources = resources;
    this.template = template;
    this.frame = frame;

    StyleInjectorHelper.ensureInjected(resources.style(), true);
  }

  @Override
  public void mask(XElement parent, String message) {
    XElement mask = XElement.createElement("div");
    mask.setClassName(resources.style().mask());
    parent.appendChild(mask);

    XElement box = null;
    if (message != null) {
      SafeHtmlBuilder sb = new SafeHtmlBuilder();
      SafeHtml content = template.template(resources.style(), SafeHtmlUtils.htmlEscape(message));
      frame.render(sb, Frame.EMPTY_FRAME, content);
      box = XDOM.create(sb.toSafeHtml()).cast();
      parent.appendChild(box);
    }

    if (SVC.isIE() && "auto".equals(parent.getStyle().getHeight())) {
      mask.setSize(parent.getOffsetWidth(), parent.getOffsetHeight());
    }

    if (box != null) {
      box.updateZIndex(0);
      box.center(parent);
    }
  }

  @Override
  public String masked() {
    return resources.style().masked();
  }

  @Override
  public String positioned() {
    return resources.style().positioned();
  }

  @Override
  public void unmask(XElement parent) {
    XElement mask = parent.selectNode("> ." + resources.style().mask());
    if (mask != null) {
      mask.removeFromParent();
    }
    XElement box = parent.selectNode("> ." + resources.style().contentArea());
    if (box != null) {
      box.removeFromParent();
    }
  }
}
