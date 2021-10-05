package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;
import ru.sbsoft.shared.grid.condition.Comparison;

/**
 *
 * @author Kiselev
 */
public class CGt<V extends Comparable<V>> extends _Cmp<V> implements IColumnConditionFactory<V> {

    private static final Comparison CMP = Comparison.GT;

    public CGt(V val) {
        super(null, CMP, val);
    }

    public CGt(ColumnInfo<V> col2) {
        super(null, CMP, col2);
    }

    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }

}
