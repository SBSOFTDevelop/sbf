/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import java.util.logging.Logger;

import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.Style.Anchor;
import ru.sbsoft.svc.core.client.Style.AnchorAlignment;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Point;
import ru.sbsoft.svc.core.client.util.Rectangle;

/**
 * A layout container that centers its single widget.
 * 
 * <p/>
 * Code Snippet:
 * 
 * <pre>
  public void onModuleLoad() {
    CenterLayoutContainer c = new CenterLayoutContainer();
    c.add(new Label("Hello world"));
    Viewport v = new Viewport();
    v.add(c);
    RootPanel.get().add(v);
  }
 * </pre>
 */
public class CenterLayoutContainer extends SimpleContainer {

  private static Logger logger = Logger.getLogger(CenterLayoutContainer.class.getName());

  @Override
  protected void doLayout() {
    if (widget != null) {
      if (SVCLogConfiguration.loggingIsEnabled()) {
        logger.finest("doLayout");
      }
      XElement con = getContainerTarget();
      XElement e = widget.getElement().cast();
      e.makePositionable(true);
      Point p = e.getAlignToXY(con, new AnchorAlignment(Anchor.CENTER, Anchor.CENTER), 0, 0);
      p = e.translatePoints(p);
      applyLayout(widget, new Rectangle(p.getX(), p.getY(), -1, -1));
    }
  }

}
