package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import ru.sbsoft.shared.consts.Formats;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#TIMESTAMP}.
 * @author sychugin
 */
public class TimestampColumnInfo extends TemporalColumnInfo {

	public TimestampColumnInfo() {
		super(ColumnType.TIMESTAMP);
		setFormat(Formats.DATE_TIME_MEDIUM);
	}

	@Override
	public Date read(ResultSet resultSet) throws SQLException {
		final Timestamp value = resultSet.getTimestamp(alias);
		if (value == null) {
			return null;
		}
		return new java.util.Date(value.getTime());
	}
}
