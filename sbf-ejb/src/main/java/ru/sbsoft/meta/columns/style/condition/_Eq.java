package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.EqCellCondition;
import ru.sbsoft.shared.grid.condition.expression.ConstGridExpression;
import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.grid.condition.expression.SimpleGridExpression;

/**
 *
 * @author Kiselev
 */
abstract class _Eq<V> {

    ColumnInfo<V> col;
    private ColumnInfo<V> col2;
    private V val;
    
    public _Eq(ColumnInfo<V> col, V val) {
        this.col = col;
        this.val = val;
    }
    
    public _Eq(ColumnInfo<V> col, ColumnInfo<V> col2) {
        this.col = col;
        this.col2 = col2;
    }
    
    public EqCellCondition createCondition() {
        IGridExpression<V> e1 = new SimpleGridExpression<>(col.getAlias());
        IGridExpression<V> e2 = col2 != null ? new SimpleGridExpression<V>(col2.getAlias()) : new ConstGridExpression<>(val);
        EqCellCondition cond = new EqCellCondition(e1, e2);
        return cond;
    }
    
}
