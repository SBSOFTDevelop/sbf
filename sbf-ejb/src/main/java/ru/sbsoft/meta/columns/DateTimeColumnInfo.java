package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#DATE_TIME}.
 * @author balandin
 * @since Nov 18, 2015
 */
public class DateTimeColumnInfo extends TemporalColumnInfo {

	public DateTimeColumnInfo() {
		super(ColumnType.DATE_TIME);
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
