/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client.dom.impl;

import java.util.List;

import com.google.gwt.dom.client.Element;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.shared.FastMap;

public class ComputedStyleImplIE extends ComputedStyleImpl {

  @Override
  public FastMap<String> getStyleAttribute(Element elem, List<String> names) {
    return getComputedStyle(elem, names, checkCamelCache(names), null, null);
  }

  @Override
  public String getStyleAttribute(Element elem, String prop) {
    return getComputedStyle(elem, checkCamelCache(prop), null, null);
  }

  @Override
  public void setStyleAttribute(Element elem, String name, Object value) {
    if (!SVC.isIE10() && "opacity".equals(name)) {
      setOpacity(elem, Double.valueOf((String.valueOf(value))));
    } else {
      super.setStyleAttribute(elem, name, value);
    }
  }

  @Override
  protected native String getComputedStyle(Element elem, String name, String name2, String psuedo) /*-{
    var v, cs;
    if (name == "opacity") {
      if (typeof elem.style.filter == "string") {
        var m = elem.style.filter.match(/alpha\(opacity=(.*)\)/i);
        if (m) {
          v = parseFloat(m[1]);
          if (!isNaN(v)) {
            return String(v ? v / 100 : 0);
          }
        }
      }
      return "1";
    }
    if (v = elem.style[name]) {
      return v;
    } else if ((cs = elem.currentStyle) && (v = cs[name])) {
      return v;
    }
    return null;
  }-*/;

  @Override
  protected native FastMap<String> getComputedStyle(Element elem, List<String> originals, List<String> names, List<String> names2, String pseudo) /*-{
    var map = @ru.sbsoft.svc.core.shared.FastMap::new()();
    var size = originals.@java.util.List::size()()
    for(var i = 0;i<size;i++){
      var name = names.@java.util.List::get(I)(i);
      var original = originals.@java.util.List::get(I)(i);

      if(name == "opacity"){
        if(typeof elem.style.filter == "string"){
          var m = elem.style.filter.match(/alpha\(opacity=(.*)\)/i);
          if(m){
            var fv = parseFloat(m[1]);
            if(!isNaN(fv)){
              map.@ru.sbsoft.svc.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(fv ? fv / 100 : 0));
              continue;
            }
          }
        }
        map.@ru.sbsoft.svc.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(1));
        continue;
      }

      var v, cs;
      if(v = elem.style[name]){
        map.@ru.sbsoft.svc.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,String(v));
      } else if(cs = elem.currentStyle) {
        map.@ru.sbsoft.svc.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original, cs[name] ? String(cs[name]) : null);
      } else {
        map.@ru.sbsoft.svc.core.shared.FastMap::put(Ljava/lang/String;Ljava/lang/Object;)(original,null);
      }
    }
    return map;
  }-*/;

  @Override
  protected String getPropertyName(String name) {
    if ("float".equals(name)) {
      return "styleFloat";
    }
    return name;
  }

  protected native void setOpacity(Element dom, double opacity)/*-{
    dom.style.zoom = 1;
    dom.style.filter = (dom.style.filter || '').replace(/alpha\([^\)]*\)/gi,"") + (opacity == 1 ? "" : " alpha(opacity=" + opacity * 100 + ")");
  }-*/;

}
