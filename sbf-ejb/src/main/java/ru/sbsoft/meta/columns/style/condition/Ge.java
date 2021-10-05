package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;
import ru.sbsoft.shared.grid.condition.Comparison;

/**
 *
 * @author Kiselev
 */
public class Ge<V extends Comparable<V>> extends _Cmp<V> implements IGridConditionFactory {

    private static final Comparison CMP = Comparison.GTE;

    public Ge(ColumnInfo<V> col, V val) {
        super(col, CMP, val);
    }

    public Ge(ColumnInfo<V> col, ColumnInfo<V> col2) {
        super(col, CMP, col2);
    }

}
