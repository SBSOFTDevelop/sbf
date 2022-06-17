package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.param.DTO;

/**
 * Условие, определяемое на основании строки браузера. Создано для условного примененя стилей к ячейкам и строкам браузера.
 * @author Kiselev
 */
public interface IGridCondition extends DTO {
    boolean isMatch(Row row);
}
