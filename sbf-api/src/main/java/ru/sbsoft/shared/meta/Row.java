package ru.sbsoft.shared.meta;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Класс представляет строку грида, содержащую коллекцию столбцов, экземпляр класса {@link IColumns}.
 * @author balandin
 */
public class Row extends MarkModel {

    private List<?> values;
    private transient IColumns columns;

    public Row() {
    }

    public List<?> getValues() {
        return values;
    }

    public void setValues(List<?> values) {
        this.values = values;
    }

    public IColumns getColumns() {
        return columns;
    }

    public void setColumns(IColumns columns) {
        this.columns = columns;
    }

    public Object getValue(String alias) {
        if (null == columns) {  // если колонки не определены, значит идет перезагрузка грида - просто возвращаем null
            return null;
        }
        final IColumn column = columns.getColumnForAlias(alias);
        if (column == null) {
            throw new ApplicationException(SBFExceptionStr.columnNotDefined, new NonLocalizedString(alias));
        }
        return values.get(column.getIndex());
    }

    public String getString(String alias) {
        return (String) getValue(alias);
    }

    public Long getLong(String alias) {
        return (Long) getValue(alias);
    }

    public Integer getInteger(String alias) {
        final Long value = getLong(alias);
        return value == null ? null : new Integer(value.intValue());
    }

    public Boolean getBoolean(String alias) {
        return (Boolean) getValue(alias);
    }

    public BigDecimal getBigDecimal(String alias) {
        return (BigDecimal) getValue(alias);
    }

    public Date getDate(String alias) {
        return (Date) getValue(alias);
    }

    public BigDecimal getPrimaryKeyValue() {
        return getBigDecimal(columns.getPrimaryKey().getAlias());
    }
}
