package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class SubstrCellCondition implements IGridCondition {

    private IGridExpression<String> expr;
    private String substr;
    private boolean caseSensitive = true;

    private SubstrCellCondition() {
    }

    public SubstrCellCondition(IGridExpression<String> expr, String substr) {
        if(substr == null){
            throw new IllegalArgumentException("Substring can't be null or empty");
        }
        this.expr = expr;
        this.substr = substr;
    }

    public SubstrCellCondition setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    @Override
    public boolean isMatch(Row row) {
        String val = expr.getValue(row);
        if(val == null){
            return false;
        }
        String sub = substr;
        if(!caseSensitive){
            val = val.toUpperCase();
            sub = sub.toUpperCase();
        }
        return val.contains(sub);
    }

}
