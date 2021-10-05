package ru.sbsoft.meta.columns.style;

import ru.sbsoft.shared.grid.condition.IGridCondition;

/**
 *
 * @author Kiselev
 */
public interface IConditionFactory {
    IGridCondition createCondition(); 
}
