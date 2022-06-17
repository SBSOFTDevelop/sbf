/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.window;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.theme.neptune.client.base.panel.Css3FramedPanelAppearance;
import ru.sbsoft.svc.widget.core.client.Window.WindowAppearance;

public class Css3WindowAppearance extends Css3FramedPanelAppearance implements WindowAppearance {

  public interface Css3WindowResources extends Css3FramedPanelResources {
    @Override
    @Source("Css3Window.gss")
    Css3WindowStyle style();
  }

  public interface Css3WindowStyle extends Css3FramedPanelStyle {
    String ghost();
  }

  private final Css3WindowResources resources;

  private final Css3WindowStyle style;

  public Css3WindowAppearance() {
    this(GWT.<Css3WindowResources>create(Css3WindowResources.class));
  }

  public Css3WindowAppearance(Css3WindowResources resources) {
    this(resources, GWT.<FramedPanelTemplate>create(FramedPanelTemplate.class));
  }

  public Css3WindowAppearance(Css3WindowResources resources, FramedPanelTemplate template) {
    super(resources, template);
    this.resources = resources;
    this.style = this.resources.style();
  }

  @Override
  public int getFrameHeight(XElement parent) {
    return Math.max(theme.window().borderRadius(), theme.window().border().top())
        + Math.max(theme.window().borderRadius(), theme.window().border().bottom());
  }

  @Override
  public int getFrameWidth(XElement parent) {
    return Math.max(theme.window().borderRadius(), theme.window().border().left())
        + Math.max(theme.window().borderRadius(), theme.window().border().right());
  }

  @Override
  public String ghostClass() {
    return style.ghost();
  }

}
