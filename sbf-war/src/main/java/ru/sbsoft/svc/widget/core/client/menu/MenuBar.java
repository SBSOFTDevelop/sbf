/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.Style.Anchor;
import ru.sbsoft.svc.core.client.Style.AnchorAlignment;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.gestures.TapGestureRecognizer;
import ru.sbsoft.svc.core.client.gestures.TouchData;
import ru.sbsoft.svc.core.client.util.KeyNav;
import ru.sbsoft.svc.widget.core.client.container.InsertContainer;
import ru.sbsoft.svc.widget.core.client.event.HideEvent;
import ru.sbsoft.svc.widget.core.client.event.HideEvent.HideHandler;
import ru.sbsoft.svc.widget.core.client.event.MaximizeEvent;
import ru.sbsoft.svc.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import ru.sbsoft.svc.widget.core.client.event.MinimizeEvent;
import ru.sbsoft.svc.widget.core.client.event.MinimizeEvent.MinimizeHandler;

public class MenuBar extends InsertContainer {

  public static interface MenuBarAppearance {

    void render(SafeHtmlBuilder builder);
  }

  class Handler implements HideHandler, MaximizeHandler, MinimizeHandler {

    @Override
    public void onHide(HideEvent event) {
      autoSelect = false;
      autoSelect = true;
      if (active != null) active.expanded = false;
    }

    @Override
    public void onMaximize(MaximizeEvent event) {
      int index = getWidgetIndex(active);
      index = index != getWidgetCount() - 1 ? index + 1 : 0;
      MenuBarItem item = (MenuBarItem) getWidget(index);
      setActiveItem(item, true);
    }

    @Override
    public void onMinimize(MinimizeEvent event) {
      int index = getWidgetIndex(active);
      index = index > 0 ? index - 1 : getWidgetCount() - 1;
      MenuBarItem item = (MenuBarItem) getWidget(index);
      setActiveItem(item, true);
    }

  }

  protected MenuBarItem active;

  private final MenuBarAppearance appearance;

  private boolean autoSelect = true;
  private Handler handler = new Handler();

  public MenuBar() {
    this(GWT.<MenuBarAppearance>create(MenuBarAppearance.class));
  }

  public MenuBar(MenuBarAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.ONFOCUS | Event.ONBLUR);

    getElement().setTabIndex(-1);
    getElement().setAttribute("hideFocus", "true");

    new KeyNav(this) {
      @Override
      public void onKeyPress(NativeEvent evt) {
        MenuBar.this.onKeyPress(evt);
      }
    };

    // Ignore Menu preview event hiding menubar when toggle click occurs.
    addStyleName("x-ignore");


    addGestureRecognizer(new TapGestureRecognizer() {
      @Override
      public boolean handleEnd(NativeEvent endEvent) {
        super.handleEnd(endEvent);
        onClick(endEvent.<Event>cast());
        return true;
      }
    });
  }

  public MenuBarAppearance getAppearance() {
    return appearance;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONCLICK:
        onClick(event);
        break;
      case Event.ONMOUSEOVER:
        onMouseOver(event);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(event);
        break;
      case Event.ONFOCUS:
        if (autoSelect && active == null && getWidgetCount() > 0) {
          setActiveItem((MenuBarItem) getWidget(0), false);
        }
        break;
      case Event.ONBLUR:
        if (active != null && !active.expanded) {
          onDeactivate(active);
        }
        break;
    }
  }

  /**
   * Sets the active item.
   * 
   * @param item the item to activate
   * @param expand true to expand the item's menu
   */
  public void setActiveItem(final MenuBarItem item, boolean expand) {
    // shouldn't automatically select first item on touch since it disables that item from being selected
    setActiveItem(item, expand, !SVC.isTouch());
  }

  /**
   * Sets the active item.
   * 
   * @param item the item to activate
   * @param expand true to expand the item's menu
   * @param selectFirst sets the first item enabled or disabled
   */
  public void setActiveItem(final MenuBarItem item, boolean expand, boolean selectFirst) {
    if (active != item) {
      if (active != null) {
        onDeactivate(active);
      }
      onActivate(item);

      if (expand) {
        expand(item, selectFirst);
      }
    }
  }

  /**
   * Toggles the given item.
   * 
   * @param item the item to toggle
   */
  public void toggle(MenuBarItem item) {
    if (item == active) {
      if (item.expanded) {
        collapse(item);
      } else {
        expand(item, false);
      }
    } else {
      setActiveItem(item, true);
    }
  }

  protected void collapse(MenuBarItem item) {
    item.menu.hide();
    item.expanded = false;
  }

  protected void expand(MenuBarItem item, boolean selectFirst) {
    item.menu.setOnHideFocusElement(getFocusEl());
    item.menu.setFocusOnShow(false);
    item.menu.show(item.getElement(), new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT), 0, 1);
    item.expanded = true;
    if (item.menu.getWidgetCount() > 0 && selectFirst) {
      item.menu.setActiveItem(item.menu.getWidget(0), false);
    }
  }

  protected void onActivate(MenuBarItem item) {
    active = item;
    // Accessibility.setState(getElement(), "aria-activedescendant",
    // item.getId());

    item.getAppearance().onActive(item.getElement(), true);
    // item.addStyleName(item.getBaseStyle() + "-active");
    // item.addStyleName(item.getBaseStyle() + "-over");
  }

  protected void onClick(Event event) {
    event.stopPropagation();
    event.preventDefault();

    MenuBarItem item = (MenuBarItem) findWidget(event.getEventTarget().<Element> cast());
    if (item != null) {
      toggle(item);
    }
  }

  protected void onDeactivate(MenuBarItem item) {
    if (item.expanded) {
      item.menu.hide();
      item.expanded = false;
    }

    item.getAppearance().onActive(item.getElement(), false);
    item.getAppearance().onOver(item.getElement(), false);
    // item.removeStyleName(item.getBaseStyle() + "-active");
    // item.removeStyleName(item.getBaseStyle() + "-over");
    // if (SVC.isFocusManagerEnabled()) {
    // FocusFrame.get().unframe();
    // }
    if (active == item) {
      active = null;
    }
  }

  protected void onDown(NativeEvent event) {
    if (active != null && getWidgetCount() > 0) {
      event.preventDefault();
      event.stopPropagation();
      if (active.expanded) {
        active.menu.focus();
        active.menu.setActiveItem(active.menu.getWidget(0), false);
      } else {
        expand(active, true);
      }
    }
  }

  @Override
  protected void onInsert(int index, Widget child) {
    super.onInsert(index, child);
    MenuBarItem item = (MenuBarItem) child;
    Menu itemMenu = item.getMenu();
    itemMenu.addHideHandler(handler);
    itemMenu.addMaximizeHandler(handler);
    itemMenu.addMinimizeHandler(handler);
  }

  protected void onKeyPress(NativeEvent evt) {
    switch (evt.getKeyCode()) {
      case KeyCodes.KEY_DOWN:
        onDown(evt);
        break;
      case KeyCodes.KEY_LEFT:
        onLeft(evt);
        break;

      case KeyCodes.KEY_RIGHT:
        onRight(evt);
        break;

    }

  }

  protected void onLeft(NativeEvent event) {
    if (active != null && getWidgetCount() > 1) {
      int idx = getWidgetIndex(active);
      idx = idx != 0 ? idx - 1 : getWidgetCount() - 1;
      MenuBarItem item = (MenuBarItem) getWidget(idx);
      setActiveItem(item, true, true);
    }
  }

  protected void onMouseOut(Event event) {
    EventTarget eventTarget = event.getEventTarget();
    if ((eventTarget == null || (Element.is(eventTarget) && findWidget((Element) Element.as(eventTarget)) == null))
        && active != null && !active.expanded) {
      onDeactivate(active);
    }
  }

  protected void onMouseOver(Event event) {
    EventTarget eventTarget = event.getEventTarget();
    if (eventTarget != null) {
      Element element = eventTarget.<Element> cast();
      if (element != null) {
        MenuBarItem item = (MenuBarItem) findWidget(element);
        if (item != null && item != active) {
          setActiveItem(item, active != null && active.expanded, false);
        }
      }
    }
  }

  protected void onRight(NativeEvent event) {
    if (active != null && getWidgetCount() > 1) {
      int idx = getWidgetIndex(active);
      idx = idx != getWidgetCount() - 1 ? idx + 1 : 0;
      MenuBarItem item = (MenuBarItem) getWidget(idx);
      setActiveItem(item, true, true);
    }
  }

}
