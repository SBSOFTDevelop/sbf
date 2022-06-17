/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.info;

import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import ru.sbsoft.svc.core.client.Style.HideMode;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Point;
import ru.sbsoft.svc.core.client.util.Size;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * Displays a message in the bottom right region of the browser for a specified amount of time.
 */
public class Info extends Component {

  public abstract static interface InfoAppearance {

    XElement getContentElement(XElement parent);

    void render(SafeHtmlBuilder sb);

  }

  private static Stack<Info> infoCache = new Stack<Info>();

  private static Stack<Info> topLeft = new Stack<Info>();
  private static Stack<Info> topRight = new Stack<Info>();
  private static Stack<Info> bottomLeft = new Stack<Info>();
  private static Stack<Info> bottomRight = new Stack<Info>();

  /**
   * Displays a message using the specified configuration
   * 
   * @param config the info configuration
   */
  public static void display(InfoConfig config) {
    pop().show(config);
  }

  /**
   * Displays a message with the given title and text.
   * 
   * @param title the title as text
   * @param message the message as text
   */
  public static void display(String title, String message) {
    display(new DefaultInfoConfig(title, message));
  }

  /**
   * Displays a message with the given title and content.
   * 
   * @param title the title as HTML
   * @param message the message as HTML
   */
  public static void display(SafeHtml title, SafeHtml message) {
    display(new DefaultInfoConfig(title, message));
  }

  private static Info pop() {
    Info info = infoCache.size() > 0 ? (Info) infoCache.pop() : null;
    if (info == null) {
      info = new Info();
    }
    return info;
  }

  private static void push(Info info) {
    infoCache.push(info);
  }

  protected InfoConfig config;

  private InfoAppearance appearance;
  private HandlerRegistration showHandlerRegistration;
  private HandlerRegistration hideHandlerRegistration;

  /**
   * Creates a new info instance.
   */
  public Info() {
    this((InfoAppearance) GWT.create(InfoAppearance.class));
  }

  /**
   * Creates a new info appearance.
   * 
   * @param appearance the info appearance
   */
  public Info(InfoAppearance appearance) {
    this.appearance = appearance;
    setElement(Document.get().createDivElement());

    setHideMode(HideMode.OFFSETS);
  }

  /**
   * Returns the appearance.
   * 
   * @return the appearance
   */
  public InfoAppearance getAppearance() {
    return appearance;
  }

  @Override
  public void hide() {
    super.hide();
    afterHide();
  }

  /**
   * Displays the info.
   * 
   * @param config the info configuration.
   */
  public void show(InfoConfig config) {
    this.config = config;

    if (showHandlerRegistration != null) {
      showHandlerRegistration.removeHandler();
      showHandlerRegistration = null;
    }

    if (hideHandlerRegistration != null) {
      hideHandlerRegistration.removeHandler();
      hideHandlerRegistration = null;
    }

    if (config.getShowHandler() != null) {
      showHandlerRegistration = addShowHandler(config.getShowHandler());
    }

    if (config.getHideHandler() != null) {
      hideHandlerRegistration = addHideHandler(config.getHideHandler());
    }

    onShowInfo();
  }

  protected void afterHide() {
    RootPanel.get().remove(this);
    topLeft.remove(this);
    topRight.remove(this);
    bottomLeft.remove(this);
    bottomRight.remove(this);
    push(this);
  }

  protected void afterShow() {
    Timer t = new Timer() {
      public void run() {
        hide();
      }
    };
    t.schedule(config.getDisplay());
  }

  protected void onShowInfo() {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);
    getElement().setInnerSafeHtml(sb.toSafeHtml());

    XElement target = appearance.getContentElement(getElement());
    target.setInnerSafeHtml(config.render(this));

    getElement().makePositionable(true);

    RootPanel.get().add(this);

    setWidth(config.getWidth());
    setHeight(config.getHeight());

    getElement().updateZIndex(0);

    Point p = position();
    getElement().setLeftTop(p.getX(), p.getY());

    show();

    afterShow();
  }

  protected Point position() {
    Size viewport = XDOM.getViewportSize();

    int scrollLeft = XDOM.getBodyScrollLeft();
    int scrollTop = XDOM.getBodyScrollTop();

    int margin = config.getMargin();

    int left = 0;
    int top = 0;

    switch (config.getPosition()) {
      case TOP_LEFT:
        if (topLeft.size() == 0) {
          left = margin + scrollLeft;
          top = margin;

        } else {
          Info prev = topLeft.peek();
          top = prev.getAbsoluteTop() + prev.getOffsetHeight() + margin;
          left = prev.getAbsoluteLeft();

          int bottom = top + getOffsetHeight();

          if (bottom > viewport.getHeight()) {
            top = config.getMargin();
            left = prev.getAbsoluteLeft() + config.getWidth() + margin + scrollLeft;
          }

        }
        topLeft.add(this);
        break;
      case TOP_RIGHT:
        if (topRight.size() == 0) {
          left = viewport.getWidth() - config.getWidth() - margin + scrollLeft;
          top = config.getMargin();

        } else {
          Info prev = topRight.peek();
          top = prev.getAbsoluteTop() + prev.getOffsetHeight() + margin;
          left = prev.getAbsoluteLeft();

          int bottom = top + getOffsetHeight();

          if (bottom > viewport.getHeight()) {
            top = margin;
            left = prev.getAbsoluteLeft() - config.getWidth() - margin + scrollLeft;
          }
        }
        topRight.add(this);
        break;
      case BOTTOM_LEFT: {
        int height = config.getHeight() == -1 ? getOffsetHeight() : config.getHeight();

        if (bottomLeft.size() == 0) {
          top = viewport.getHeight() - height - margin + scrollTop;
          left = margin + scrollLeft;

        } else {
          Info prev = bottomLeft.peek();
          left = prev.getAbsoluteLeft() + scrollLeft;
          top = prev.getAbsoluteTop() - height - margin;

          if (top < 0) {
            top = viewport.getHeight() - height - margin + scrollTop;
            left = prev.getAbsoluteLeft() + prev.getOffsetWidth() + margin + scrollLeft;
          }
        }
        bottomLeft.add(this);
      }
        break;
      case BOTTOM_RIGHT: {
        int height = config.getHeight() == -1 ? getOffsetHeight() : config.getHeight();
        if (bottomRight.size() == 0) {
          top = viewport.getHeight() - height - margin - XDOM.getBodyScrollTop();
          left = viewport.getWidth() - config.getWidth() - margin + scrollLeft - XDOM.getScrollBarWidth();
        } else {
          Info prev = bottomRight.peek();
          top = prev.getAbsoluteTop() - height - margin;
          left = prev.getAbsoluteLeft() + scrollLeft;

          if (top < 0) {
            top = viewport.getHeight() - height - margin + scrollTop;
            left = prev.getAbsoluteLeft() - prev.getOffsetWidth() - margin + scrollLeft;
          }
        }
        bottomRight.add(this);
      }
        break;
    }

    return new Point(left, top);
  }

}
