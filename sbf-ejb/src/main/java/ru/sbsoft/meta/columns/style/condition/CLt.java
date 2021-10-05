package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IColumnConditionFactory;
import ru.sbsoft.shared.grid.condition.Comparison;

/**
 *
 * @author Kiselev
 */
public class CLt<V extends Comparable<V>> extends _Cmp<V> implements IColumnConditionFactory<V> {

    private static final Comparison CMP = Comparison.LT;

    public CLt(V val) {
        super(null, CMP, val);
    }

    public CLt(ColumnInfo<V> col2) {
        super(null, CMP, col2);
    }

    @Override
    public void setColumn(ColumnInfo<V> col) {
        this.col = col;
    }
    
}
