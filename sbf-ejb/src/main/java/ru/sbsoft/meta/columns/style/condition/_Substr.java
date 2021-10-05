package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.condition.SubstrCellCondition;
import ru.sbsoft.shared.grid.condition.expression.SimpleGridExpression;

/**
 *
 * @author Kiselev
 */
abstract class _Substr {

    ColumnInfo<String> col;
    private final String substr;

    public _Substr(ColumnInfo<String> col, String substr) {
        this.col = col;
        this.substr = substr;
    }

    public IGridCondition createCondition() {
        return new SubstrCellCondition(new SimpleGridExpression<String>(col.getAlias()), substr).setCaseSensitive(false);
    }

}
