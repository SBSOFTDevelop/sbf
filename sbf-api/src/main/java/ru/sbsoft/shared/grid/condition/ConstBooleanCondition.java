package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class ConstBooleanCondition implements IGridCondition {
    private boolean condResult = false;

    private ConstBooleanCondition() {
    }

    public ConstBooleanCondition(boolean condResult) {
        this.condResult = condResult;
    }

    @Override
    public boolean isMatch(Row row) {
        return condResult;
    }
    
}
