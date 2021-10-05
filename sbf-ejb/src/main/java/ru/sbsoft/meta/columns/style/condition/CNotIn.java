package ru.sbsoft.meta.columns.style.condition;

import java.util.Set;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;

/**
 *
 * @author Kiselev
 */
public class CNotIn<V> extends _In<V> implements IColumnConditionFactory<V> {

    public CNotIn(Set<V> vals) {
        super(null, vals, true);
    }

    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }
}
