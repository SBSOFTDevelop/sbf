package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 *
 * @author Kiselev
 */
public class Ne<V> extends _Ne<V> implements IGridConditionFactory {

    public Ne(ColumnInfo<V> col, V val) {
        super(col, val);
    }

    public Ne(ColumnInfo<V> col, ColumnInfo<V> col2) {
        super(col, col2);
    }
}
