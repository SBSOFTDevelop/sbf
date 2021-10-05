package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 *
 * @author Kiselev
 */
public class NotNull extends _NotNull implements IGridConditionFactory  {

    public NotNull(ColumnInfo col) {
        super(col);
    }
    
}
