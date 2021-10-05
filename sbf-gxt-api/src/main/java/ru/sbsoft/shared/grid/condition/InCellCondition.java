package ru.sbsoft.shared.grid.condition;

import java.util.Set;
import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 * @param <V>
 */
public class InCellCondition<V> implements IGridCondition {
    private IGridExpression<V> expr1;
    private Set<V> vals;
    private boolean notIn = false;

    private InCellCondition() {
    }
    
    public InCellCondition(IGridExpression<V> expr1, Set<V> vals) {
        this.expr1 = expr1;
        this.vals = vals;
    }

    public InCellCondition<V> setNotIn() {
        this.notIn = true;
        return this;
    }

    @Override
    public boolean isMatch(Row row) {
        V val1 = expr1.getValue(row);
        boolean in = vals != null && val1 != null && vals.contains(val1);
        return notIn ? !in : in;
    }
    
}
