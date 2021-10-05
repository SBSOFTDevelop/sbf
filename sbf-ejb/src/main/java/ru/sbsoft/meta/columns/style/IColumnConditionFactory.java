package ru.sbsoft.meta.columns.style;

import ru.sbsoft.meta.columns.ColumnInfo;

/**
 *
 * @author Kiselev
 */
public interface IColumnConditionFactory<V> extends IConditionFactory {

    void setColumn(ColumnInfo<V> col);
}
