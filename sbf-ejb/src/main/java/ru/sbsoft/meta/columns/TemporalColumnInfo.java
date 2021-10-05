package ru.sbsoft.meta.columns;

import java.util.Date;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа Date.
 * @author balandin
 * @since Jun 6, 2014 2:01:49 PM
 */
public abstract class TemporalColumnInfo extends ColumnInfo<Date> {

	public TemporalColumnInfo(ColumnType type) {
		super(type);
	}

	@Override
	public String getXlsTypeHint() {
		return "x:date";
	}
}
