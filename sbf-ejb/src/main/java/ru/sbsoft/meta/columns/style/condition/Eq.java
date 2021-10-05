package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 *
 * @author Kiselev
 */
public class Eq<V> extends _Eq<V> implements IGridConditionFactory {

    public Eq(ColumnInfo<V> col, V val) {
        super(col, val);
    }

    public Eq(ColumnInfo<V> col, ColumnInfo<V> col2) {
        super(col, col2);
    }

}
