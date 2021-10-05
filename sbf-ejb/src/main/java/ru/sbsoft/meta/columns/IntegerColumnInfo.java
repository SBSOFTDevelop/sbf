package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#INTEGER}.
 * @author balandin
 * @since Mar 18, 2014 4:39:10 PM
 */
public class IntegerColumnInfo extends ColumnInfo<Long> {

    public IntegerColumnInfo() {
        super(ColumnType.INTEGER);
    }

    @Override
    public Long read(ResultSet resultSet) throws SQLException {
        final long value = resultSet.getLong(alias);
        if (resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    @Override
    public String getXlsTypeHint() {
        return "x:num";
    }
}
