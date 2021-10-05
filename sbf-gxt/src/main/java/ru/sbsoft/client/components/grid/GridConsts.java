package ru.sbsoft.client.components.grid;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.client.model.IMarkProperties;

/**
 * Значения по умолчанию для таблицы.
 * @author balandin
 * @since Oct 3, 2013 6:29:37 PM
 */
public interface GridConsts {

	public final static IMarkProperties MARK_PROP = GWT.create(IMarkProperties.class);
	public final static int ROW_CACHE_SIZE = 200;
	public final static int AUTO_EXPAND_MIN_VALUE = 200;
	public final static int AUTO_EXPAND_MAX_VALUE = 1920;
}