package ru.sbsoft.meta.columns;

import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#TEMPORAL_KEY}.
 * @author balandin
 * @since Mar 28, 2014 10:44:57 AM
 */
public class TemporalKeyColumnInfo extends NumberColumnInfo {

	public TemporalKeyColumnInfo() {
		super(ColumnType.TEMPORAL_KEY);
	}
}
