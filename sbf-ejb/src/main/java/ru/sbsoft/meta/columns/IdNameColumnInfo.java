package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.util.IdNameLong;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#ADDRESS}.
 *
 * @author balandin
 * @since Mar 5, 2014 6:52:28 PM
 */
public class IdNameColumnInfo extends ColumnInfo<IdNameLong> {

    public IdNameColumnInfo() {
        super(ColumnType.ID_NAME);
    }

    /**
     * Метод вызывается построителем запросов экземпляром класса {@link ru.sbsoft.meta.sql.SQLBuilder}.
     * <p>
     * Через вызов метода sb.append(String) происходит формирование строки SQL <code> select fields0 as alias,...fieldsN as alias </code>.
     *
     * @param sb <code>StringBuilder</code>
     */
    @Override
    public void buildSelectClause(StringBuilder sb) {
        super.buildSelectClause(sb);
        String nc = getNameClause();
        if (nc != null && !(nc = nc.trim()).isEmpty()) {
            sb.append(",").append(nc).append(" AS ").append(getNameClauseAlias());
        }
    }

    private String getNameClauseAlias() {
        return alias + "_name";
    }

    @Override
    public IdNameLong read(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(alias);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return new IdNameLong(id, resultSet.getString(getNameClauseAlias()));
        }
    }
}
