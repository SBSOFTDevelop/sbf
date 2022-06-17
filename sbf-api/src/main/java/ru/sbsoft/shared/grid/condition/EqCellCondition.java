package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 * @param <V>
 */
public class EqCellCondition<V> implements IGridCondition {
    private IGridExpression<V> expr1;
    private IGridExpression<V> expr2;
    private boolean notEq = false;

    private EqCellCondition() {
    }
    
    public EqCellCondition(IGridExpression<V> expr1, IGridExpression<V> expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public EqCellCondition<V> setNotEq() {
        this.notEq = true;
        return this;
    }

    @Override
    public boolean isMatch(Row row) {
        V val1 = expr1.getValue(row);
        V val2 = expr2.getValue(row);
        boolean eq = val1 == val2 ? true : val1 != null && val1.equals(val2);
        return notEq ? !eq : eq;
    }
    
}
