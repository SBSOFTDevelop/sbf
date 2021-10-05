package ru.sbsoft.shared.grid.condition.expression;

import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class SimpleGridExpression<V> implements IGridExpression<V> {
    private String columnAlias;

    private SimpleGridExpression() {
    }

    public SimpleGridExpression(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    @Override
    public V getValue(Row row) {
        return (V)row.getValue(columnAlias);
    }

}
