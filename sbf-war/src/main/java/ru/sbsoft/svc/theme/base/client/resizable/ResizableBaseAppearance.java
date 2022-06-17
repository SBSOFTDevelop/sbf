/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.resizable;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.Resizable.Dir;
import ru.sbsoft.svc.widget.core.client.Resizable.ResizableAppearance;

public class ResizableBaseAppearance implements ResizableAppearance {

  public interface ResizableResources extends ClientBundle {
    @Source("Resizable.gss")
    ResizableStyle style();

  }

  public interface ResizableStyle extends CssResource {

    String handle();

    String handleEast();

    String handleNorth();

    String handleNortheast();

    String handleNorthwest();

    String handleSouth();

    String handleSoutheast();

    String handleSouthwest();

    String handleWest();

    String over();

    String overlay();

    String pinned();

    String proxy();

  }

  private final ResizableResources resources;
  private final ResizableStyle style;

  public ResizableBaseAppearance() {
    this(GWT.<ResizableResources>create(ResizableResources.class));
  }

  public ResizableBaseAppearance(ResizableResources resources) {
    this.resources = resources;
    this.style = resources.style();
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public Element createProxy() {
    XElement elem = Document.get().createDivElement().cast();
    elem.addClassName(resources.style().proxy());
    elem.disableTextSelection(true);
    return elem;
  }

  public String getHandleStyles(Dir dir) {
    StringBuilder styleBuilder = new StringBuilder();
    styleBuilder.append(style.handle());
    styleBuilder.append(' ');
    switch (dir) {
      case E:
        styleBuilder.append(style.handleEast());
        break;
      case N:
        styleBuilder.append(style.handleNorth());
        break;
      case W:
        styleBuilder.append(style.handleWest());
        break;
      case S:
        styleBuilder.append(style.handleSouth());
        break;
      case NE:
        styleBuilder.append(style.handleNortheast());
        break;
      case NW:
        styleBuilder.append(style.handleNorthwest());
        break;
      case SW:
        styleBuilder.append(style.handleSouthwest());
        break;
      case SE:
        styleBuilder.append(style.handleSoutheast());
        break;
    }
    return styleBuilder.toString();
  }

}
