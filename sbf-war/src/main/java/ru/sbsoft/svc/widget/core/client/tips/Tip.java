/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.tips;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.RootPanel;
import ru.sbsoft.svc.core.client.Style.Side;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.util.Point;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.ComponentHelper;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * This is the base class for {@link ToolTip} that provides the basic layout and
 * positioning that all tip-based classes require. This class can be used
 * directly for simple, statically-positioned tips that are displayed
 * programmatically, or it can be extended to provide custom tip
 * implementations.
 */
public class Tip extends Component {

  public interface TipAppearance {
    void applyAnchorDirectionStyle(XElement anchorEl, Side anchor);

    void applyAnchorStyle(XElement anchorEl);

    XElement getBodyElement(XElement parent);

    XElement getToolsElement(XElement parent);

    void removeAnchorStyle(XElement anchorEl);

    void render(SafeHtmlBuilder sb);

    void updateContent(XElement parent, SafeHtml title, SafeHtml body);

  }

  protected int quickShowInterval = 250;
  protected boolean constrainPosition = true;
  protected ToolButton close;
  protected boolean showing;

  private final TipAppearance appearance;

  private int minWidth = 40;
  private int maxWidth = 300;
  private boolean closable;

  /**
   * Creates a new tip instance.
   */
  public Tip() {
    this((TipAppearance) GWT.create(TipAppearance.class));
  }

  public Tip(TipAppearance appearance) {
    this.appearance = appearance;
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));

    shim = true;

    setShadow(true);

    getElement().makePositionable(true);
  }

  /**
   * Returns the tip's appearance.
   * 
   * @return the appearance
   */
  public TipAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the maximum width.
   * 
   * @return the max width
   */
  public int getMaxWidth() {
    return maxWidth;
  }

  /**
   * Returns the minimum width.
   * 
   * @return the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  @Override
  public void hide() {
    super.hide();
    if (isAttached()) {
      RootPanel.get().remove(this);
    }
  }

  /**
   * Returns true if the tip is closable.
   * 
   * @return the closable state
   */
  public boolean isClosable() {
    return closable;
  }

  /**
   * True to render a close tool button into the tooltip header (defaults to
   * false).
   * 
   * @param closable the closable state
   */
  public void setClosable(boolean closable) {
    this.closable = closable;
    appearance.getToolsElement(getElement()).removeChildren();
    if (closable) {
      if (close == null) {
        close = new ToolButton(ToolButton.CLOSE);
        close.addSelectHandler(new SelectHandler() {
          @Override
          public void onSelect(SelectEvent event) {
            hide();
          }
        });
        if (isAttached()) {
          ComponentHelper.doAttach(close);
        }
      }
      appearance.getToolsElement(getElement()).appendChild(close.getElement());
    }
  }

  /**
   * Sets the maximum width of the tip in pixels (defaults to 300). The maximum
   * supported value is 500.
   * 
   * @param maxWidth the max width
   */
  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }

  /**
   * Sets the minimum width of the tip in pixels (defaults to 40).
   * 
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * Shows this tip at the specified position.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void showAt(int x, int y) {
    // tip is "moving" when mousing over target with displayed closable tip as
    // position is calculated based on event x,y coords
    if (isAttached() && closable) {
      return;
    }

    getElement().setVisibility(false);
    if (!isAttached()) {
      RootPanel.get().add(this);
    }

    // need to show before constrain calls as offset will be 0 if hidden
    showing = true;
    super.show();
    showing = false;

    updateContent();

    getElement().makePositionable(true);
    getElement().updateZIndex(0);

    Point p = new Point(x, y);
    if (constrainPosition) {
      p = getElement().adjustForConstraints(p);
    }
    setPagePosition(p.getX() + XDOM.getBodyScrollLeft(), p.getY() + XDOM.getBodyScrollTop());
    getElement().setVisibility(true);

    sync(true);
  }

  /**
   * Shows this tip at the specified position.
   * 
   * @param point the position
   */
  public void showAt(Point point) {
    showAt(point.getX(), point.getY());
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(close);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(close);
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    hide();
  }

  protected void updateContent() {
  }

}
