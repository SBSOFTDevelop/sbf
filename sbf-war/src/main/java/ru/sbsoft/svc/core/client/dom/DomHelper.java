/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Utility class for creating elements from HTML fragments.
 */
public class DomHelper {
  static {
    Ext.loadExt();
    Ext.loadDomHelper();
  }

  /**
   * Creates new DOM element(s) and appends them to el.
   * 
   * @param elem the context element
   * @param html the html
   * @return the new element
   */
  public static native Element append(Element elem, SafeHtml html) /*-{
    var Ext = @ru.sbsoft.svc.core.client.dom.Ext::ext;
		return Ext.DomHelper.append(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false);
  }-*/;

  /**
   * Creates new DOM element(s) and inserts them after el.
   * 
   * @param elem the context element
   * @param html rthe html
   * @return the new element
   */
  public static native Element insertAfter(Element elem, SafeHtml html) /*-{
    var Ext = @ru.sbsoft.svc.core.client.dom.Ext::ext;
		return Ext.DomHelper.doInsert(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false,
        "afterEnd", "nextSibling");
  }-*/;

  /**
   * Creates new DOM element(s) and inserts them before el.
   * 
   * @param elem the context element
   * @param html the html
   * @return the new element
   */
  public static native Element insertBefore(Element elem, SafeHtml html) /*-{
		var Ext = @ru.sbsoft.svc.core.client.dom.Ext::ext;
		return Ext.DomHelper.doInsert(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false, "beforeBegin");
  }-*/;

  /**
   * Creates new DOM element(s) and inserts them as the first child of el.
   * 
   * @param elem the context element
   * @param html the html
   * @return the new element
   */
  public static native Element insertFirst(Element elem, SafeHtml html) /*-{
		var Ext = @ru.sbsoft.svc.core.client.dom.Ext::ext;
		return Ext.DomHelper.doInsert(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()(), false,
        "afterBegin", "firstChild");
  }-*/;

  /**
   * Inserts an HTML fragment into the DOM.
   * 
   * @param where where to insert the html in relation to el - beforeBegin,
   *          afterBegin, beforeEnd, afterEnd.
   * @param el the context element
   * @param html the html
   * @return the inserted node (or nearest related if more than 1 inserted)
   */
  public static native Element insertHtml(String where, Element el, SafeHtml html) /*-{
    var h = html.@com.google.gwt.safehtml.shared.SafeHtml::asString()();
    if (!h)
      return el;

    var Ext = @ru.sbsoft.svc.core.client.dom.Ext::ext;
    return Ext.DomHelper.insertHtml(where, el, h);
  }-*/;

  /**
   * Creates new DOM element(s) and overwrites the contents of el with them.
   * 
   * @param elem the context element
   * @param html the html
   * @return the first new element
   */
  public static native Element overwrite(Element elem, SafeHtml html) /*-{
    var Ext = @ru.sbsoft.svc.core.client.dom.Ext::ext;
		return Ext.DomHelper.overwrite(elem, html.@com.google.gwt.safehtml.shared.SafeHtml::asString()());
  }-*/;
  
}
