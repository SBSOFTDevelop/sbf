package ru.sbsoft.meta.columns.style.condition;

import java.util.Set;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.InCellCondition;
import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.grid.condition.expression.SimpleGridExpression;

/**
 *
 * @author Kiselev
 */
abstract class _In<V> {

    ColumnInfo<V> col;
    private Set<V> vals;
    private boolean notIn = false;

    _In(ColumnInfo<V> col, Set<V> vals) {
        this(col, vals, false);
    }
    
    _In(ColumnInfo<V> col, Set<V> vals, boolean notIn) {
        this.col = col;
        this.vals = vals;
        this.notIn = notIn;
    }

    public InCellCondition createCondition() {
        IGridExpression<V> e1 = new SimpleGridExpression<>(col.getAlias());
        InCellCondition cond = new InCellCondition(e1, vals);
        if(notIn){
            cond.setNotIn();
        }
        return cond;
    }
}
