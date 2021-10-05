package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.style.IGridConditionFactory;
import ru.sbsoft.shared.grid.condition.ConstBooleanCondition;
import ru.sbsoft.shared.grid.condition.IGridCondition;

/**
 *
 * @author vk
 */
public class ConstTrue implements IGridConditionFactory {

    @Override
    public IGridCondition createCondition() {
        return new ConstBooleanCondition(true);
    }
    
}
