package ru.sbsoft.client.utils;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import java.util.HashMap;

/**
 * Поиск {@link PredefinedFormat} по имени.
 * @author balandin
 * @since May 12, 2014 3:23:32 PM
 */
public class PredefinedFormatHelper {

	private static HashMap<String, PredefinedFormat> tmp;

	private PredefinedFormatHelper() {
	}

	public static PredefinedFormat find(String name) {
		if (tmp == null) {
			final PredefinedFormat[] values = PredefinedFormat.values();
			tmp = new HashMap<String, PredefinedFormat>(values.length);
			for (PredefinedFormat item : values) {
				tmp.put(item.name(), item);
			}
		}
		return tmp.get(name);
	}
}
