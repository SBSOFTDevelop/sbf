/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.theme.base.client.frame.DivFrame;
import ru.sbsoft.svc.theme.base.client.frame.DivFrame.DivFrameResources;
import ru.sbsoft.svc.theme.base.client.frame.DivFrame.DivFrameStyle;
import ru.sbsoft.svc.theme.base.client.frame.Frame;
import ru.sbsoft.svc.widget.core.client.info.Info.InfoAppearance;

public class InfoDefaultAppearance implements InfoAppearance {

  public interface InfoResources extends ClientBundle, DivFrameResources {

    @Source("background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("bottomBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bottomBorder();

    @Source("bottomLeftBorder.png")
    ImageResource bottomLeftBorder();

    @Source("bottomRightBorder.png")
    ImageResource bottomRightBorder();

    @Source("leftBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource leftBorder();

    @Source("rightBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource rightBorder();

    @Source({"ru/sbsoft/svc/theme/base/client/frame/DivFrame.gss", "Info.gss"})
    InfoStyle style();

    @Source("topBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource topBorder();

    @Source("topLeftBorder.png")
    ImageResource topLeftBorder();

    @Source("topRightBorder.png")
    ImageResource topRightBorder();
  }

  public interface InfoStyle extends DivFrameStyle {
    String info();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "InfoDefault.html")
    SafeHtml render(InfoStyle style);
  }

  private final Template template;
  private final Frame frame;
  private final InfoStyle style;

  public InfoDefaultAppearance() {
    this(GWT.<Template>create(Template.class), GWT.<InfoResources>create(InfoResources.class));
  }

  public InfoDefaultAppearance(Template template, InfoResources resources) {
    this.template = template;
    this.style = resources.style();
    this.style.ensureInjected();

    frame = new DivFrame(resources);
  }

  @Override
  public XElement getContentElement(XElement parent) {
    return parent.selectNode("." + style.info());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

}