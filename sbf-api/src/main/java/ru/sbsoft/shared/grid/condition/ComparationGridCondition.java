package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import static ru.sbsoft.shared.grid.condition.Comparison.*;
import ru.sbsoft.shared.meta.Row;
/**
 *
 * @author Kiselev
 * @param <V> значение выражения
 */
public class ComparationGridCondition<V extends Comparable<V>> implements IGridCondition {

    private IGridExpression<V> expr1;
    private IGridExpression<V> expr2;
    private Comparison cmp;

    private ComparationGridCondition() {
    }

    public ComparationGridCondition(IGridExpression<V> expr1, IGridExpression<V> expr2, Comparison cmp) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.cmp = cmp;
    }

    @Override
    public boolean isMatch(Row row) {
        V val1 = expr1.getValue(row);
        V val2 = expr2.getValue(row);
        if (val1 == null || val2 == null) {
            if (in(cmp, EQ, GTE, LTE)) {
                return val1 == val2;
            }
            if (cmp == NE) {
                return val1 == null ^ val2 == null;
            }
            return false;
        } else {
            int res = val1.compareTo(val2);
            return res == 0 && in(cmp, EQ, GTE, LTE) || res > 0 && in(cmp, NE, GT, GTE) || res < 0 && in(cmp, NE, LT, LTE);
        }
    }

    private boolean in(Comparison cmp, Comparison... cmpList) {
        for (Comparison cmp0 : cmpList) {
            if (cmp0 == cmp) {
                return true;
            }
        }
        return false;
    }

}
