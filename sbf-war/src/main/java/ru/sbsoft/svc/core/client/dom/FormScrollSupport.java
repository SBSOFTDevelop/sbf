package ru.sbsoft.svc.core.client.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class FormScrollSupport implements ScrollSupport {

  private XElement scrollElement;

  private ScrollMode scrollMode = ScrollMode.NONE;

  public FormScrollSupport(Element scrollElement) {
    this.scrollElement = scrollElement.cast();
  }

  @Override
  public void ensureVisible(UIObject item) {
    Element element = item.getElement();
    ensureVisibleImpl(scrollElement, element);
  }

  @Override
  public int getHorizontalScrollPosition() {
    return scrollElement.getScrollLeft();
  }

  @Override
  public int getMaximumHorizontalScrollPosition() {
    return ScrollImplHelper.getMaximumHorizontalScrollPosition(scrollElement);
  }

  @Override
  public int getMaximumVerticalScrollPosition() {
    return scrollElement.getScrollHeight() - scrollElement.getClientHeight();
  }

  @Override
  public int getMinimumHorizontalScrollPosition() {
    return ScrollImplHelper.getMinimumHorizontalScrollPosition(scrollElement);
  }

  @Override
  public int getMinimumVerticalScrollPosition() {
    return 0;
  }

  public XElement getScrollElement() {
    return scrollElement;
  }

  @Override
  public ScrollMode getScrollMode() {
    return scrollMode;
  }

  @Override
  public int getVerticalScrollPosition() {
    return scrollElement.getScrollTop();
  }

  @Override
  public void scrollToBottom() {
    setVerticalScrollPosition(getMaximumVerticalScrollPosition());
  }

  @Override
  public void scrollToLeft() {
    setHorizontalScrollPosition(getMinimumHorizontalScrollPosition());
  }

  @Override
  public void scrollToRight() {
    setHorizontalScrollPosition(getMaximumHorizontalScrollPosition());
  }

  @Override
  public void scrollToTop() {
    setVerticalScrollPosition(getMinimumVerticalScrollPosition());
  }

  @Override
  public void setHorizontalScrollPosition(int position) {
    scrollElement.setScrollLeft(position);
  }

  @Override
  public void setScrollMode(ScrollMode scrollMode) {
    this.scrollMode = scrollMode;
    switch (scrollMode) {
      case AUTO:
      case ALWAYS:
      case NONE:
        scrollElement.getStyle().setProperty("overflowX", scrollMode.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowY", scrollMode.value().toLowerCase());
        break;
      case AUTOX:
        scrollElement.getStyle().setProperty("overflowX", scrollMode.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowY", "hidden");
        break;
      case AUTOY:
        // 3.0.4 - Делаем вертикальный скролл видимым всегда
        // scrollElement.getStyle().setProperty("overflowY", scrollMode.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowY", "scroll");
        scrollElement.getStyle().setProperty("overflowX", "hidden");
    }
  }
  
  @Override
  public void setVerticalScrollPosition(int position) {
    scrollElement.setScrollTop(position);
  }

  private native void ensureVisibleImpl(Element scroll, Element e) /*-{
		if (!e)
			return;

		var item = e;
		var realOffset = 0;
		while (item && (item != scroll)) {
			realOffset += item.offsetTop;
			item = item.offsetParent;
		}

		scroll.scrollTop = realOffset - scroll.offsetHeight / 2;
  }-*/;

}
