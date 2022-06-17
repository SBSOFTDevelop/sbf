package ru.sbsoft.shared.grid.condition.expression;

import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class ConstGridExpression<V> implements IGridExpression<V>{
    private V val;

    private ConstGridExpression() {
    }

    public ConstGridExpression(V val) {
        this.val = val;
    }

    @Override
    public V getValue(Row row) {
        return val;
    }
    
}
