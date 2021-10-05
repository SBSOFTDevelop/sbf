package ru.sbsoft.meta.columns;

import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#KEY}.
 * @author balandin
 * @since Mar 5, 2014 6:57:17 PM
 */
public class KeyColumnInfo extends NumberColumnInfo {

	public KeyColumnInfo() {
		super(ColumnType.KEY);
	}
}
