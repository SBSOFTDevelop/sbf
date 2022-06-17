package ru.sbsoft.shared.grid.condition.expression;

import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 * @param <V> - результат выражения
 */
public interface IGridExpression<V> extends DTO {
    V getValue(Row row);
}
