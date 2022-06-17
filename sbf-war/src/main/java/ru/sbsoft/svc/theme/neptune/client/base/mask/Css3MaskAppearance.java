/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.mask;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.dom.Mask.MaskAppearance;
import ru.sbsoft.svc.core.client.dom.Mask.MaskDefaultAppearance.MaskStyle;
import ru.sbsoft.svc.core.client.dom.Mask.MessageTemplates;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;

public class Css3MaskAppearance implements MaskAppearance {

  public interface Css3MaskResources extends ClientBundle {
    @Source("Css3Mask.gss")
    Css3MaskStyles styles();

    @Source("ru/sbsoft/svc/theme/base/client/grid/loading.gif")
    ImageResource loading();

    ThemeDetails theme();
  }

  public interface Css3MaskStyles extends MaskStyle {

  }

  private final Css3MaskResources resources;
  private final MessageTemplates template;

  public Css3MaskAppearance() {
    this(GWT.<Css3MaskResources>create(Css3MaskResources.class));
  }

  public Css3MaskAppearance(Css3MaskResources resources) {
    this(resources, GWT.<MessageTemplates>create(MessageTemplates.class));
  }

  public Css3MaskAppearance(Css3MaskResources resources, MessageTemplates template) {
    this.resources = resources;
    this.template = template;
    StyleInjectorHelper.ensureInjected(resources.styles(), true);
  }

  @Override
  public void mask(XElement parent, String message) {
    XElement mask = XElement.createElement("div");
    mask.setClassName(resources.styles().mask());
    parent.appendChild(mask);

    XElement box = null;
    if (message != null) {
      box = XDOM.create(template.template(resources.styles(), SafeHtmlUtils.htmlEscape(message))).cast();
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
    return resources.styles().masked();
  }

  @Override
  public String positioned() {
    return resources.styles().positioned();
  }

  @Override
  public void unmask(XElement parent) {
    XElement mask = parent.selectNode("> ." + resources.styles().mask());
    if (mask != null) {
      mask.removeFromParent();
    }
    XElement box = parent.selectNode("> ." + resources.styles().box());
    if (box != null) {
      box.removeFromParent();
    }
  }
}
