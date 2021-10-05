package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.NotNullCellCondition;

/**
 *
 * @author Kiselev
 */
abstract class _Null extends _NotNull {

    public _Null(ColumnInfo col) {
        super(col);
    }

    @Override
    public NotNullCellCondition createCondition() {
        return super.createCondition().setNullCheck();
    }
    
}
