package ru.sbsoft.meta.columns.style.condition;

import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;

/**
 *
 * @author Kiselev
 */
public class Null extends _Null implements IGridConditionFactory {

    public Null(ColumnInfo col) {
        super(col);
    }

}
