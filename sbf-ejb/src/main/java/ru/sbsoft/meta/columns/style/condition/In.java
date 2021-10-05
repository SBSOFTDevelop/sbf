package ru.sbsoft.meta.columns.style.condition;

import java.util.Set;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 *
 * @author Kiselev
 */
public class In<V> extends _In<V> implements IGridConditionFactory {

    public In(ColumnInfo<V> col, Set<V> vals) {
        super(col, vals);
    }

}
