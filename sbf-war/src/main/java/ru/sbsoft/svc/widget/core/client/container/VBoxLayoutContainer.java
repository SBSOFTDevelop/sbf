/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.container;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.CommonStyles;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.core.client.util.Size;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * A layout container for vertical columns of widgets.
 * <p/>
 * Code Snippet:
 * 
 * <pre>
    VBoxLayoutContainer c = new VBoxLayoutContainer();
    c.setVBoxLayoutAlign(VBoxLayoutAlign.LEFT);
    BoxLayoutData layoutData = new BoxLayoutData(new Margins(5, 0, 0, 5));
    c.add(new TextButton("Button 1"), layoutData);
    c.add(new TextButton("Button 2"), layoutData);
    c.add(new TextButton("Button 3"), layoutData);
    Viewport v = new Viewport();
    v.add(c);
    RootPanel.get().add(v);
 * </pre>
 */
public class VBoxLayoutContainer extends BoxLayoutContainer {
  /**
   * Alignment enumeration for horizontal alignment.
   */
  public enum VBoxLayoutAlign {
    /**
     * Children are aligned vertically at the <b>mid-width</b> of the container.
     */
    CENTER,
    /**
     * Children are aligned vertically at the <b>left</b> side of the container
     * (default).
     */
    LEFT,
    /**
     * Children are aligned vertically at the <b>right</b> side of the container
     * (default).
     */
    RIGHT,
    /**
     * Children are stretched horizontally to fill the width of the container.
     */
    STRETCH,
    /**
     * Children widths are set the size of the largest child's width.
     */
    STRETCHMAX
  }

  private VBoxLayoutAlign vBoxLayoutAlign;

  private static Logger logger = Logger.getLogger(VBoxLayoutContainer.class.getName());

  /**
   * Creates a vbox layout.
   */
  public VBoxLayoutContainer() {
    this(VBoxLayoutAlign.LEFT);
  }

  /**
   * Creates a vbox layout with the specified alignment.
   * 
   * @param align the horizontal alignment
   */
  public VBoxLayoutContainer(VBoxLayoutAlign align) {
    super();
    setVBoxLayoutAlign(align);
  }

  /**
   * Returns the horizontal alignment.
   * 
   * @return the horizontal alignment
   */
  public VBoxLayoutAlign getVBoxLayoutAlign() {
    return vBoxLayoutAlign;
  }

  /**
   * Sets the horizontal alignment for child items (defaults to LEFT).
   * 
   * @param vBoxLayoutAlign the horizontal alignment
   */
  public void setVBoxLayoutAlign(VBoxLayoutAlign vBoxLayoutAlign) {
    this.vBoxLayoutAlign = vBoxLayoutAlign;
  }

  @Override
  protected void doLayout() {
    Size size = getElement().getStyleSize();

    if (SVCLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " doLayout  size: " + size);
    }

    int w = size.getWidth() - getScrollOffset();
    int h = size.getHeight();
    
    int styleHeight = Util.parseInt(getElement().getStyle().getProperty("height"), Style.DEFAULT);
    int styleWidth = Util.parseInt(getElement().getStyle().getProperty("width"), Style.DEFAULT);

    boolean findWidth = styleWidth == -1;
    boolean findHeight = styleHeight == -1;

    if (SVCLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " findWidth: " + findWidth + " findHeight: " + findHeight);
    }

    int calculateHeight = 0;

    int maxWidgetWidth = 0;
    int maxMarginLeft = 0;
    int maxMarginRight = 0;

    for (int i = 0, len = getWidgetCount(); i < len; i++) {
      Widget widget = getWidget(i);

      BoxLayoutData layoutData = null;
      Object d = widget.getLayoutData();
      if (d instanceof BoxLayoutData) {
        layoutData = (BoxLayoutData) d;
      } else {
        layoutData = new BoxLayoutData();
        widget.setLayoutData(layoutData);
      }

      Margins cm = layoutData.getMargins();
      if (cm == null) {
        cm = new Margins(0);
        layoutData.setMargins(cm);
      }
    }
    
    if (findWidth || findHeight) {
      for (int i = 0, len = getWidgetCount(); i < len; i++) {
        Widget widget = getWidget(i);

        if (!widget.isVisible()) {
          continue;
        }

        BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
        Margins cm = layoutData.getMargins();

        calculateHeight += widget.getOffsetHeight();
        maxWidgetWidth = Math.max(maxWidgetWidth, widget.getOffsetWidth());

        calculateHeight += (cm.getTop() + cm.getBottom());
        maxMarginLeft = Math.max(maxMarginLeft, cm.getLeft());
        maxMarginRight = Math.max(maxMarginRight, cm.getRight());
      }
      maxWidgetWidth += (maxMarginLeft + maxMarginRight);

      if (findHeight) {
        h = calculateHeight;
      }

      if (findWidth) {
        w = maxWidgetWidth;
      }
    }

    int pl = 0;
    int pt = 0;
    int pb = 0;
    int pr = 0;
    if (getPadding() != null) {
      pl = getPadding().getLeft();
      pt = getPadding().getTop();
      pb = getPadding().getBottom();
      pr = getPadding().getRight();
    }
    
    if (findHeight) {
      h += pt + pb;
    }
    if (findWidth) {
      w += pl + pr;
    }


    int stretchWidth = w - pl - pr;
    int totalFlex = 0;
    int totalHeight = 0;
    int maxWidth = 0;
    for (int i = 0, len = getWidgetCount(); i < len; i++) {
      Widget widget = getWidget(i);
      widget.addStyleName(CommonStyles.get().positionable());

      widget.getElement().getStyle().setMargin(0, Unit.PX);

      // callLayout(widget, false);

      BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
      Margins cm = layoutData.getMargins();
      
      totalFlex += layoutData.getFlex();
      totalHeight += widget.getOffsetHeight() + cm.getTop() + cm.getBottom();
      maxWidth = Math.max(maxWidth, widget.getOffsetWidth() + cm.getLeft() + cm.getRight());
    }

    int innerCtWidth = maxWidth + pl + pr;

    if (vBoxLayoutAlign.equals(VBoxLayoutAlign.STRETCH)) {
      getContainerTarget().setSize(w, h, true);
    } else {
      getContainerTarget().setSize(w = Math.max(w, innerCtWidth), h, true);
    }

    int extraHeight = h - totalHeight - pt - pb;
    int allocated = 0;
    int cw, ch, cl;
    int availableWidth = w - pl - pr;

    if (getPack().equals(BoxLayoutPack.CENTER)) {
      pt += extraHeight / 2;
    } else if (getPack().equals(BoxLayoutPack.END)) {
      pt += extraHeight;
    }

    for (int i = 0, len = getWidgetCount(); i < len; i++) {
      Widget widget = getWidget(i);

      BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
      Margins cm = layoutData.getMargins();
      
      cw = widget.getOffsetWidth();
      ch = widget.getOffsetHeight();
      pt += cm.getTop();
      if (vBoxLayoutAlign.equals(VBoxLayoutAlign.CENTER)) {
        int diff = availableWidth - (cw + cm.getLeft() + cm.getRight());
        if (diff == 0) {
          cl = pl + cm.getLeft();
        } else {
          cl = pl + cm.getLeft() + (diff / 2);
        }
      } else {
        if (vBoxLayoutAlign.equals(VBoxLayoutAlign.RIGHT)) {
          cl = w - (pr + cm.getRight() + cw);
        } else {
          cl = pl + cm.getLeft();
        }
      }

      boolean component = widget instanceof Component;
      Component c = null;
      if (component) {
        c = (Component) widget;
      }

      int height = -1;
      if (component) {
        c.setPosition(cl, pt);
      } else {
        XElement.as(widget.getElement()).setLeftTop(cl, pt);
      }

      if (getPack().equals(BoxLayoutPack.START) && layoutData.getFlex() > 0) {
        int add = (int) Math.floor(extraHeight * (layoutData.getFlex() / totalFlex));
        allocated += add;
        if (isAdjustForFlexRemainder() && i == getWidgetCount() - 1) {
          add += extraHeight - allocated;
        }
        ch += add;
        height = ch;
      }
      if (vBoxLayoutAlign.equals(VBoxLayoutAlign.STRETCH)) {
        applyLayout(
            widget,
            Util.constrain(stretchWidth - cm.getLeft() - cm.getRight(), layoutData.getMinSize(), layoutData.getMaxSize()),
            height);
      } else if (vBoxLayoutAlign.equals(VBoxLayoutAlign.STRETCHMAX)) {
        applyLayout(widget,
            Util.constrain(maxWidth - cm.getLeft() - cm.getRight(), layoutData.getMinSize(), layoutData.getMaxSize()),
            height);
      } else if (height > 0) {
        applyLayout(widget, -1, height);
      }
      pt += ch + cm.getBottom();
    }
  }

}
