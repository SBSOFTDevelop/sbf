package ru.sbsoft.meta.columns;

import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки типа {@link ru.sbsoft.shared.meta.ColumnType#CURRENCY}.
 * @author balandin
 * @since Mar 19, 2014 4:38:30 PM
 */
public class CurrencyColumnInfo extends NumberColumnInfo {

	public CurrencyColumnInfo() {
		super(ColumnType.CURRENCY);
	}
}
