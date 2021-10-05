package ru.sbsoft.shared.grid.style;

import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public class ConditionalCellStyle implements DTO {

    private CStyle style;

    private IGridCondition condition;

    private ConditionalCellStyle() {
    }
    
    public ConditionalCellStyle(CStyle style) {
        this(style, null);
    }
    
    public ConditionalCellStyle(CStyle style, IGridCondition condition) {
        this.style = style;
        this.condition = condition;
    }

    public ConditionalCellStyle setCondition(IGridCondition condition) {
        this.condition = condition;
        return this;
    }

    public CStyle getStyle() {
        return style;
    }

    public boolean isApplicable(Row row) {
        return condition == null || condition.isMatch(row);
    }

}
