package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class NotNullCellCondition implements IGridCondition {
    private IGridExpression expr;
    private boolean nullCheck = false;

    private NotNullCellCondition() {
    }

    public NotNullCellCondition(IGridExpression expr) {
        this.expr = expr;
    }

    public NotNullCellCondition setNullCheck() {
        this.nullCheck = true;
        return this;
    }

    @Override
    public boolean isMatch(Row row) {
        Object val = expr.getValue(row);
        return (val == null) ^ !nullCheck;
    }
    
}
