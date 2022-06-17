package ru.sbsoft.sbf.svc.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.client.utils.CliUtil;

/**
 *
 * @author vk
 */
public class DebugUtils {

    public static String getId(Element el) {
        String id = null;
        if (el != null) {
            id = el.getId();
            if (id.isEmpty()) {
                id = XDOM.getUniqueId();
                el.setId(id);
            }
            return id;
        }
        return id;
    }

    public static StringBuilder appendWidgetPath(Widget w, StringBuilder b) {
        b.append("Widget path: ");
        while (w != null) {
            b.append(" -> ").append(w.getClass().getName());
            w = w.getParent();
        }
        return b;
    }

    public static void printInfo(Element el, String msg) {
        StringBuilder b = new StringBuilder();
        b.append(msg).append(" Element: {").append(String.valueOf(el)).append(" ID: ").append(getId(el)).append('}');
        b.append(' ');
        DebugUtils.appendWidgetPath(CliUtil.findWidget(el), b);
        DebugUtils.console(b.toString());
    }

    public static void printWidgetPath(Widget w) {
        console(appendWidgetPath(w, new StringBuilder()).toString());
    }

    public static void printTarget(NativeEvent ev, String msg) {
        final Element el = ev.getEventTarget().cast();
        DebugUtils.printInfo(el, msg + " on target " + el.toString() + ". EventType: " + ev.getType() + " KeyCode: " + ev.getKeyCode()
                + " Modifiers:" + " Shift(" + ev.getShiftKey() + ") Alt(" + ev.getAltKey() + ") Ctrl(" + ev.getCtrlKey() + ") Meta(" + ev.getMetaKey() + ")");
    }

    public static native void console(String message) /*-{
        try {
            console.log(message);
        } catch (e) {
        }
    }-*/;
}
