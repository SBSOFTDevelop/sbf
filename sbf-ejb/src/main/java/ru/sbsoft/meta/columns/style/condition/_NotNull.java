package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.NotNullCellCondition;
import ru.sbsoft.shared.grid.condition.expression.SimpleGridExpression;

/**
 *
 * @author Kiselev
 */
abstract class _NotNull   {

    ColumnInfo col;

    public _NotNull(ColumnInfo col) {
        this.col = col;
    }
    
    public NotNullCellCondition createCondition() {
        return new NotNullCellCondition(new SimpleGridExpression<>(col.getAlias()));
    }
    
}
