package ru.sbsoft.meta.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Класс, представляющий колонки сетки типа {@link ru.sbsoft.shared.meta.ColumnType#ADDRESS}.
 *
 * @author balandin
 * @since Mar 5, 2014 6:52:28 PM
 */
public class AddressColumnInfo extends ColumnInfo<AddressModel> {

    private String[] fields;

    public AddressColumnInfo() {
        super(ColumnType.ADDRESS);
    }

    public String[] getFields() {
        return fields;
    }

    public AddressColumnInfo setFields(String[] fields) {
        this.fields = fields;
        return this;
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
        final int size = fields.length;
        for (int i = 0; i < size; i++) {

            final String f = fields[i];

            sb.append(clause).append(f);
            sb.append(" AS ");
            sb.append(alias).append(f);

            if (i < size - 1) {
                sb.append(",");
            }
        }
    }

    @Override
    public AddressModel read(ResultSet resultSet) throws SQLException {
        final AddressModel address = new AddressModel();
        address.setPostIndex(resultSet.getString(alias + fields[0]));
        address.setRegionName(resultSet.getString(alias + fields[1]));
        address.setAreaName(resultSet.getString(alias + fields[2]));
        address.setCityName(resultSet.getString(alias + fields[3]));
        address.setVillageName(resultSet.getString(alias + fields[4]));
        address.setStreetName(resultSet.getString(alias + fields[5]));
        address.setHouse(resultSet.getString(alias + fields[6]));
        address.setBlock(resultSet.getString(alias + fields[7]));
        address.setFlat(resultSet.getString(alias + fields[8]));
        return address;
    }
}
