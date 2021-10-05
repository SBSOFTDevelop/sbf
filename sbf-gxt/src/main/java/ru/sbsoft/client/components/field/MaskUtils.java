package ru.sbsoft.client.components.field;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * @author balandin
 * @since Sep 19, 2014 3:14:11 PM
 */
//TODO deprecated
public class MaskUtils {

	public static boolean SHOW_EMPTY_TEXT = true;

	private MaskUtils() {
	}

	public static void mask(Field field, String pattern, String emptytext) {
		if (!SHOW_EMPTY_TEXT || field.getCell().isReadOnly() || !field.isEnabled()) {
			emptytext = null;
		}
		mask(field.getElement(), pattern, emptytext);
	}

	public static void unmask(Field field) {
		unmask(field.getElement());
	}

	private static native void mask(Element e, String mask, String emptyText) /*-{
	 $wnd.jQuery(e).find('input').mask(mask, {"emptytext": emptyText});
	 }-*/;

	private static native void unmask(Element e) /*-{
	 $wnd.jQuery(e).find('input').unmask();
	 }-*/;
}
