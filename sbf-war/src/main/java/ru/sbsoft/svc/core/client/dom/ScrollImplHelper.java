/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom;

import com.google.gwt.dom.client.Element;

final class ScrollImplHelper {

  public native static int getMaximumHorizontalScrollPosition(Element elem) /*-{
		var c = @com.google.gwt.user.client.ui.ScrollImpl::get()();
		return c.@com.google.gwt.user.client.ui.ScrollImpl::getMaximumHorizontalScrollPosition(Lcom/google/gwt/dom/client/Element;)(elem);
  }-*/;
  
  public native static int getMinimumHorizontalScrollPosition(Element elem) /*-{
  var c = @com.google.gwt.user.client.ui.ScrollImpl::get()();
  return c.@com.google.gwt.user.client.ui.ScrollImpl::getMinimumHorizontalScrollPosition(Lcom/google/gwt/dom/client/Element;)(elem);
}-*/;

}
