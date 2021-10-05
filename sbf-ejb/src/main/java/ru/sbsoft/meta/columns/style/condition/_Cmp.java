package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.ComparationGridCondition;
import ru.sbsoft.shared.grid.condition.Comparison;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.condition.expression.ConstGridExpression;
import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.grid.condition.expression.SimpleGridExpression;

/**
 *
 * @author Kiselev
 */
abstract class _Cmp<V extends Comparable<V>> {

    ColumnInfo<V> col;
    private ColumnInfo<V> col2;
    private V val;
    private final Comparison cmp;

    _Cmp(ColumnInfo<V> col, Comparison cmp, V val) {
        this.col = col;
        this.val = val;
        this.cmp = cmp;
    }

    _Cmp(ColumnInfo<V> col, Comparison cmp, ColumnInfo<V> col2) {
        this.col = col;
        this.col2 = col2;
        this.cmp = cmp;
    }

    public IGridCondition createCondition() {
        IGridExpression<V> e1 = new SimpleGridExpression<>(col.getAlias());
        IGridExpression<V> e2 = col2 != null ? new SimpleGridExpression<V>(col2.getAlias()) : new ConstGridExpression<>(val);
        return new ComparationGridCondition(e1, e2, cmp);
    }

}
