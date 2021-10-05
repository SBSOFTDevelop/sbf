package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#VCHAR}.
 * @author balandin
 * @since Mar 5, 2014 6:58:13 PM
 */
public class StringColumnInfo extends ColumnInfo<String> {

	public StringColumnInfo() {
		super(ColumnType.VCHAR);
	}

	@Override
	public String read(ResultSet resultSet) throws SQLException {
		return resultSet.getString(alias);
	}
}
