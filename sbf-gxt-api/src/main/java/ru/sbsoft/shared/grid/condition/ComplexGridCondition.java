package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class ComplexGridCondition implements IGridCondition {

    private OrGridCondition resultCondition;
    private AndGridCondition currentAndCondition;

    public ComplexGridCondition() {
        resultCondition = new OrGridCondition();
        addNewAndCondition();
    }
    
    public ComplexGridCondition(IGridCondition condition) {
        addCondition(condition);
    }
    
    private void addNewAndCondition() {
        currentAndCondition = new AndGridCondition();
        resultCondition.add(currentAndCondition);
    }

    public final ComplexGridCondition addCondition(IGridCondition condition) {
        if (condition != null) {
            currentAndCondition.add(condition);
        }
        return this;
    }

    public final ComplexGridCondition addConditionOr(IGridCondition condition) {
        if (condition != null) {
            addNewAndCondition();
            addCondition(condition);
        }
        return this;
    }

    @Override
    public boolean isMatch(Row row) {
        return resultCondition.isMatch(row);
    }
    
}
