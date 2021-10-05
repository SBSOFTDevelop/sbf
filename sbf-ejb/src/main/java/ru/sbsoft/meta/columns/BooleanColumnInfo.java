package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа
 * {@link ru.sbsoft.shared.meta.ColumnType#BOOL}.
 *
 * @author balandin
 * @since Mar 18, 2014 2:19:12 PM
 */
public class BooleanColumnInfo extends ColumnInfo<Boolean> {

    public BooleanColumnInfo() {
        super(ColumnType.BOOL);
    }

    @Override
    public Boolean read(ResultSet resultSet) throws SQLException {
        //final int value = resultSet.getInt(alias);
        final Boolean value = resultSet.getBoolean(alias);
        if (resultSet.wasNull()) {
            return null;
        }
        //return value != 0;
        return value;
    }
}
