package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#DATE}.
 * @author balandin
 * @since Mar 5, 2014 6:56:31 PM
 */
public class DateColumnInfo extends TemporalColumnInfo {

	public DateColumnInfo() {
		super(ColumnType.DATE);
	}

	@Override
	public Date read(ResultSet resultSet) throws SQLException {
		final Date value = resultSet.getDate(alias);
		if (value == null) {
			return null;
		}
		return new java.util.Date(value.getTime());
	}
}
